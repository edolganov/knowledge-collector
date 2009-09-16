package ru.dolganov.tool.knowledge.collector.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.NodeMeta;
import ru.chapaj.util.swing.tree.ExtendTree;
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
	DefaultMutableTreeNode treeRoot;
	
	
	@Override
	public void init(final MainWindow ui) {
		tree = ui.tree;
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
		
		ui.dirB.setEnabled(false);
		ui.linkB.setEnabled(false);
		ui.noteB.setEnabled(false);
		
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
			s.node.add(new DefaultMutableTreeNode(Cell.BUTTONS));
			for(NodeMeta meta : s.list){
				DefaultMutableTreeNode chNode = createTreeNode(meta);
				s.node.add(chNode);
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
