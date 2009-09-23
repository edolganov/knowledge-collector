package ru.dolganov.tool.knowledge.collector.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import model.knowledge.Dir;
import model.knowledge.NodeMeta;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.chapaj.util.swing.tree.TreeNodeAdapter;
import ru.chapaj.util.swing.tree.ExtendTree.SelectModel;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.dao.DAOEventAdapter;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;
import ru.dolganov.tool.knowledge.collector.tree.cell.HasCellConst;
import ru.dolganov.tool.knowledge.collector.tree.cell.MainCellEditor;
import ru.dolganov.tool.knowledge.collector.tree.cell.MainCellRender;

public class TreeController extends Controller<MainWindow> implements HasCellConst{

	private static final String TREE_NODE = "tree-node";
	ExtendTree tree;
	JTextField path;
	
	DefaultMutableTreeNode treeRoot;
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
				DefaultMutableTreeNode treeNode = dao.getCache().get(parent, TREE_NODE, DefaultMutableTreeNode.class);
				tree.addChild(treeNode, createTreeNode(child));
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
		
		
		fillTree();
	}


	protected void setPathInfo(DefaultMutableTreeNode node) {
		StringBuilder sb = new StringBuilder();
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
		long time = System.currentTimeMillis();
		
		LinkedList<QS> q = new LinkedList<QS>();
		q.addLast(new QS(dao.getRoot().getNodes(),treeRoot));
		while(!q.isEmpty()){
			QS s = q.removeFirst();
			DefaultMutableTreeNode node = s.node;
			Object ob = node.getUserObject();
//			if(ob instanceof Dir){
//				node.add(new DefaultMutableTreeNode(Cell.BUTTONS));
//			}
			for(NodeMeta meta : s.list){
				DefaultMutableTreeNode chNode = createTreeNode(meta);
				node.add(chNode);
				q.addLast(new QS(dao.getChildren(meta),chNode));
			}
		}
		System.out.println("tree filled after "
				+ ((System.currentTimeMillis() - time) / 1000.) + " sec");
		
		tree.expandPath(treeRoot);
		tree.updateUI();
		
	}

	private DefaultMutableTreeNode createTreeNode(NodeMeta meta) {
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(meta);
		dao.getCache().put(meta,TREE_NODE, treeNode);
		return treeNode;
	}
	
	public void addNode(NodeMeta node){
		if(node == null) return;
		DefaultMutableTreeNode currentNode = tree.getCurrentNode();
		if(currentNode == null) return;
		
		Object userObject = currentNode.getUserObject();
		if(userObject == null) return;
		
		if(Cell.BUTTONS == userObject){
			NodeMeta parent = tree.getParentObject(currentNode, NodeMeta.class);
			if(parent == null) return;
			dao.addChild(parent, node);
		}
		else if (userObject instanceof NodeMeta) {
			dao.addChild((NodeMeta)userObject, node);
		}
	}

	public void deleteCurrentNode() {
		NodeMeta node = getCurMeta();
		if(node == null) return;
		dao.delete(node);
	}

	public void updateCurrentNode(Map<String, String> params) {
		NodeMeta node = getCurMeta();
		if(node == null) return;
		dao.update(node,params);
	}
	
	
	
	
	private NodeMeta getCurMeta(){
		Object ob = tree.getCurrentObject();
		if(ob == null) return null;
		
		if (ob instanceof NodeMeta) {
			return (NodeMeta) ob;
		}
		
		return null;
	}

	
}
