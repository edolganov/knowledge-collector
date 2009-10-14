package ru.dolganov.tool.knowledge.collector.tree;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import model.knowledge.Dir;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.NodeMeta;
import model.knowledge.Root;
import model.knowledge.TextData;
import ru.chapaj.util.collection.ListUtil;
import ru.chapaj.util.lang.ClassUtil;
import ru.chapaj.util.swing.tree.ExtendDefaultTreeModel;
import ru.chapaj.util.swing.tree.TreeNodeAdapter;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.dao.DAOEventAdapter;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;

@ControllerInfo(target=MainWindow.class, dependence=TreeController.class)
public class SortTreeController extends Controller<MainWindow>{
	
	
	private Comparator<NodeMeta> nodeComparator = new Comparator<NodeMeta>(){

		@Override 
		public int compare(NodeMeta a,NodeMeta b){
			int na = index(a);
			int nb = index(b);
			return (na<nb ? -1 : (na==nb ? 0 : 1));
		}
		
		private int index(NodeMeta meta){
			int out = Integer.MAX_VALUE;
			if(meta instanceof Dir) out = 0;
			else if(meta instanceof TextData) out = 10;
			else if(meta instanceof LocalLink) out = 20;
			else if(meta instanceof NetworkLink) out = 30;
			return out;
		}
		
	};
	
	MainWindow ui;
	
	@Override
	public void init(MainWindow ui_) {
		ui = ui_;
		get(TreeController.class).addListener(new DAOEventAdapter(){

			@Override
			public void onAdded(NodeMeta parent, NodeMeta child) {
				sortNodes(parent, child);
			}
		});
		
		ui.tree.addTreeNodeListener(new TreeNodeAdapter(){
			@Override
			public void onNodeMoveDownRequest() {
				moveNode(true);
			}
			
			@Override
			public void onNodeMoveUpRequest() {
				moveNode(false);
			}
			
			@Override
			public void afterDrop(DefaultMutableTreeNode tagretNode,
					DefaultMutableTreeNode draggedNode) {
				TreeOps.move(tagretNode,draggedNode);
			}
		});
		
	}

	private void sortNodes(NodeMeta parent, NodeMeta child) {
		//update model
		DefaultMutableTreeNode childInitNode = dao.getCache().get(child, "tree-node", DefaultMutableTreeNode.class);
		TreePath childPath = new TreePath(childInitNode.getPath());
		TreePath selectedPath = null;
		if(ui.tree.isPathSelected(childPath)){
			selectedPath = childPath;
		}
		Root root = child.getParent();
		List<NodeMeta> nodes = root.getNodes();
		Collections.sort(nodes, nodeComparator);
		dao.merge(root);
		//update tree
		DefaultMutableTreeNode parentNode = dao.getCache().get(parent, "tree-node", DefaultMutableTreeNode.class);

		for (int i = 0; i < nodes.size(); i++) {
			DefaultMutableTreeNode childNode = dao.getCache().get(nodes.get(i), "tree-node", DefaultMutableTreeNode.class);
			if(parentNode.getChildAt(i) != childNode){
				parentNode.remove(childNode);
				parentNode.insert(childNode, i);
			}					
		}
		ui.tree.model().reload(parentNode);
		if(selectedPath != null) ui.tree.setSelectionPath(selectedPath);
	}

	private void moveNode(boolean down) {
		DefaultMutableTreeNode node = ui.tree.getCurrentNode();
		if(node == null) return;
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		if(parent == null || parent.getChildCount() == 1) return;
		Object ob = node.getUserObject();
		if(!(ob instanceof NodeMeta)) return;
		
		NodeMeta meta = (NodeMeta) ob;
		Root root = meta.getParent();
		List<NodeMeta> nodes = root.getNodes();
		
		int oldIndex = nodes.indexOf(meta);
		int minIndex = oldIndex;
		Class<? extends NodeMeta> nodeClass = meta.getClass();
		for(int i = oldIndex -1 ; i > -1; i--){
			minIndex = i;
			if(!ClassUtil.isValid(nodes.get(i).getClass(), nodeClass)){
				++minIndex;
				break;
			}
		}
		int maxIndex = oldIndex;
		for(int i = oldIndex +1 ; i < nodes.size(); i++){
			maxIndex = i;
			if(!ClassUtil.isValid(nodes.get(i).getClass(), nodeClass)){
				--maxIndex;
				break;
			}
		}
		if(minIndex == maxIndex) return;
		int newIndex = 0;
		if(down) newIndex = oldIndex + 1;
		else newIndex = oldIndex - 1;
		if(newIndex > maxIndex) newIndex = minIndex;
		if(newIndex < minIndex) newIndex = maxIndex;
		//System.out.println("oldIndex:"+oldIndex + " newIndex:" + newIndex);
		//update model
		ListUtil.move(nodes, meta, newIndex);
		dao.merge(root);
		//update tree
		ExtendDefaultTreeModel model = ui.tree.model();
		model.removeNodeFromParent(node);
		model.insertNodeInto(node, parent, newIndex);
		TreePath treePath = new TreePath(node.getPath());
		ui.tree.setSelectionPath(treePath);
	}
	

}
