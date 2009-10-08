package ru.dolganov.tool.knowledge.collector.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import model.knowledge.Dir;
import model.knowledge.NodeMeta;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.chapaj.util.swing.tree.TreeNodeAdapter;
import ru.chapaj.util.swing.tree.ExtendTree.SelectModel;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.dao.DAOEventAdapter;
import ru.dolganov.tool.knowledge.collector.dao.DAOEventListener;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;
import ru.dolganov.tool.knowledge.collector.tree.cell.HasCellConst;
import ru.dolganov.tool.knowledge.collector.tree.cell.MainCellEditor;
import ru.dolganov.tool.knowledge.collector.tree.cell.MainCellRender;

@ControllerInfo(target=MainWindow.class)
public class TreeController extends Controller<MainWindow> implements HasCellConst{

	private static final String TREE_NODE = "tree-node";
	ExtendTree tree;
	JTextField path;
	
	DefaultMutableTreeNode treeRoot;
	LinkedList<DAOEventListener> listeners = new LinkedList<DAOEventListener>();
//	DefaultMutableTreeNode lastDirNode;
//	DefaultMutableTreeNode buttons = new DefaultMutableTreeNode(Cell.BUTTONS);
	
	
	@Override
	public void init(final MainWindow ui) {
		tree = ui.tree;
		path = ui.path;
		path.setEditable(false);
		//path.setBackground(Color.WHITE);
		
		tree.init(
				ExtendTree.createTreeModel(null), 
				true, 
				new MainCellRender(), 
				SelectModel.SINGLE,
				new TreeMenu(tree));
		

		
		treeRoot = tree.getRootNode();
		treeRoot.setUserObject("root");
		tree.setRootVisible(false);
		
		tree.setCellEditor(new MainCellEditor());
		tree.setEditable(true);
		
		tree.addTreeNodeListener(new TreeNodeAdapter(){
			
			@Override
			public void onNodeSelect(DefaultMutableTreeNode node) {
				setPathInfo(node);
//				if(buttons == node) {
//					//select next node
//					return;
//				}
//				
//				if(node == null) return;
//				Object ob = node.getUserObject();
//				if(ob == null) return;
//				
//				if(ob instanceof Dir && tree.isExpanded(node)){
//					if(lastDirNode != null){
//						DefaultMutableTreeNode child = (DefaultMutableTreeNode)lastDirNode.getChildAt(0);
//						if(buttons == child){
//							tree.model().removeNodeFromParent(child);
//						}
//						lastDirNode = null;
//					}
//					
//					node.insert(buttons, 0);
//					tree.model().reload(node);
//					lastDirNode = node;
//				}
			}
			
		});
		
		dao.addListener(new DAOEventAdapter(){

			@Override
			public void onAdded(NodeMeta parent, NodeMeta child) {
				DefaultMutableTreeNode parentNode = dao.getCache().get(parent, TREE_NODE, DefaultMutableTreeNode.class);
				DefaultMutableTreeNode childNode = dao.getCache().get(child, TREE_NODE, DefaultMutableTreeNode.class);
				if(childNode != null){
					DefaultMutableTreeNode oldParent = (DefaultMutableTreeNode)childNode.getParent();
					parentNode.add(childNode);
					tree.model().reload(oldParent);
					tree.model().reload(parentNode);
					TreePath path = new TreePath(childNode.getPath());
					tree.setSelectionPath(path);
					tree.scrollPathToVisible(path);
				} else {
					DefaultMutableTreeNode createTreeNode = createTreeNode(child);
					tree.addChild(parentNode, createTreeNode);
					tree.setSelectionPath(createTreeNode);
				}
				for(DAOEventListener l : listeners) l.onAdded(parent, child);
			}
			
			@Override
			public void onDeleted(NodeMeta node) {
				DefaultMutableTreeNode treeNode = dao.getCache().get(node, TREE_NODE, DefaultMutableTreeNode.class);
				tree.model().removeNodeFromParent(treeNode);
			}
			
			@Override
			public void onUpdated(NodeMeta node) {
				DefaultMutableTreeNode treeNode = dao.getCache().get(node, TREE_NODE, DefaultMutableTreeNode.class);
				treeNode.setUserObject(node);
				tree.model().reload(treeNode);
			}
			
		});
		path.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(curNode != null){
					tree.scrollPathToVisible(new TreePath(curNode.getPath()));
				}
			}
		});
		
		fillTree();
	}


	DefaultMutableTreeNode curNode;
	protected void setPathInfo(DefaultMutableTreeNode node) {
		StringBuilder sb = new StringBuilder();
		curNode = node;
		if(node != null){
			TreeNode[] path = node.getPath();
			if(path.length > 2){
				if(path.length == 3){
					Object ob = ((DefaultMutableTreeNode)path[1]).getUserObject();
					if(ob instanceof Dir){
						sb.append('/').append(path[2].toString());
					}
				}
				else {
					int last = path.length - 1;
					for (int i = 2; i < last ; i++) {
						sb.append('/').append(path[i].toString());
					}
					sb.append('/').append(path[last].toString());
				}
			}
		}
		String pathString = sb.toString();
		path.setText(pathString);
		path.setCaretPosition(0);
		
	}


	static class QS {
		List<NodeMeta> list;
		DefaultMutableTreeNode node;
		public QS(List<NodeMeta> list, DefaultMutableTreeNode node) {
			super();
			this.list = list;
			this.node = node;
		}
	}
	
	private void fillTree() {
		treeRoot.removeAllChildren();
		//long time = System.currentTimeMillis();
		
		LinkedList<QS> q = new LinkedList<QS>();
		q.addLast(new QS(dao.getRoot().getNodes(),treeRoot));
		while(!q.isEmpty()){
			QS s = q.removeFirst();
			DefaultMutableTreeNode node = s.node;
//			Object ob = node.getUserObject();
//			if(ob instanceof Dir){
//				node.add(new DefaultMutableTreeNode(Cell.BUTTONS));
//			}
			for(NodeMeta meta : s.list){
				DefaultMutableTreeNode chNode = createTreeNode(meta);
				node.add(chNode);
				q.addLast(new QS(dao.getChildren(meta),chNode));
			}
		}
		//System.out.println("tree filled after "+ ((System.currentTimeMillis() - time) / 1000.) + " sec");
		
		tree.expandPath(treeRoot);
		tree.updateUI();
		
	}

	private DefaultMutableTreeNode createTreeNode(NodeMeta meta) {
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(meta);
		dao.getCache().put(meta,TREE_NODE, treeNode);
		return treeNode;
	}
	
	public void addListener(DAOEventListener listener){
		listeners.add(listener);
	}
	
}



//public void deleteCurrentNode() {
//NodeMeta node = getCurMeta();
//if(node == null) return;
//dao.delete(node);
//}
//
//public void updateCurrentNode(Map<String, String> params) {
//NodeMeta node = getCurMeta();
//if(node == null) return;
//dao.update(node,params);
//}

///**
//* пришел к пониманию что {@link Actions} лучше для этого подходят
//* @param node
//*/
//@Deprecated
//public void addNode(NodeMeta node){
//if(node == null) return;
//DefaultMutableTreeNode currentNode = tree.getCurrentNode();
//if(currentNode == null) return;
//
//Object userObject = currentNode.getUserObject();
//if(userObject == null) return;
//
//if(Cell.BUTTONS == userObject){
//	NodeMeta parent = tree.getParentObject(currentNode, NodeMeta.class);
//	if(parent == null) return;
//	dao.addChild(parent, node);
//}
//else if (userObject instanceof NodeMeta) {
//	dao.addChild((NodeMeta)userObject, node);
//}
//}
