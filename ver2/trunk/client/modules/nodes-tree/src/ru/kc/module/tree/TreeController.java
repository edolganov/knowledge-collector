package ru.kc.module.tree;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.FocusProvider;
import ru.kc.common.controller.Controller;
import ru.kc.common.node.command.UpdateNode;
import ru.kc.common.node.edit.event.NodeChanged;
import ru.kc.common.node.edit.event.NodeReverted;
import ru.kc.common.node.event.ChildMoved;
import ru.kc.common.tree.event.SelectNodeRequest;
import ru.kc.model.Node;
import ru.kc.module.tree.tools.CellEditor;
import ru.kc.module.tree.tools.CellRender;
import ru.kc.module.tree.tools.TreeMenu;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.update.UpdateName;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.util.swing.tree.MenuController;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class TreeController extends Controller<Tree> implements FocusProvider {
	
	private final String treeNodeKey = "tree-node-key-"+hashCode();
	
	JTree tree;
	TreeFacade treeFacade;

	@Override
	public void init() {
		tree = ui.tree;
		treeFacade = new TreeFacade(ui.tree);
		
		tree.setRootVisible(true);
		tree.setModel(TreeFacade.createModelByUserObject(TreeFacade.createNode("")));
		tree.setCellRenderer(new CellRender(tree, context.nodeEditionsAggregator));
		final CellEditor cellEditor = new CellEditor(tree);
		cellEditor.addCustomListener(new CellEditorListener() {
			
			@Override
			public void editingStopped(ChangeEvent e) {
				Node node = treeFacade.getCurrentObject(Node.class);
				String newName = cellEditor.getCellEditorValue();
				invokeSafe(new UpdateNode(node, new UpdateName(newName)));
			}
			
			@Override
			public void editingCanceled(ChangeEvent e) {}
		});
		tree.setCellEditor(cellEditor);
		tree.setRowHeight(0);
		tree.setEditable(true);
		treeFacade.setSingleSelection();
		treeFacade.setPopupMenu(new TreeMenu(tree, appContext, context, this), new MenuController() {
			
			@Override
			public boolean canShow() {
				return ! cellEditor.isEnabled();
			}
		});

		
		
		
		buildTree();
	}

	private void buildTree() {
		try {
			Node rootNode = persistTree.getRoot();
			DefaultMutableTreeNode treeRootNode = TreeFacade.createNode(rootNode);
			addToStorage(rootNode,treeRootNode);
			tree.setModel(TreeFacade.createModelByNode(treeRootNode));

			
			
			LinkedList<DefaultMutableTreeNode> queue = new LinkedList<DefaultMutableTreeNode>();
			queue.addLast(treeFacade.getRoot());
			while(queue.size() > 0){
				DefaultMutableTreeNode treeNode = queue.removeFirst();
				Node node = (Node)treeNode.getUserObject();
				List<Node> children = node.getChildren();
				for(Node child : children){
					DefaultMutableTreeNode treeChild = addChildToTree(treeNode, child);
					queue.addLast(treeChild);
				}
			}
			tree.revalidate();
			tree.repaint();
		}catch (Exception e) {
			log.error("error init tree", e);
		}
	}
	
	@Override
	protected void onChildAdded(Node parent, Node child) {
		DefaultMutableTreeNode parentNode = getFromStorage(parent);
		if(parentNode == null){
			log.info("can't find parent tree node by "+parent);
			return;
		}
		addChildToTree(parentNode, child);
	}
	
	private DefaultMutableTreeNode addChildToTree(DefaultMutableTreeNode parentTreeNode, Node child) {
		boolean selectChild = treeFacade.isSelectedNode(parentTreeNode);
		DefaultMutableTreeNode treeChild = treeFacade.addChild(parentTreeNode, child);
		addToStorage(child,treeChild);
		if(selectChild){
			treeFacade.setSelection(treeChild);
			tree.requestFocus();
		}

		return treeChild;
	}
	
	
	@Override
	protected void onChildDeletedRecursive(Node parent, Node deletedChild, List<Node> deletedSubChildren) {
		DefaultMutableTreeNode parentNode = getFromStorage(parent);
		if(parentNode == null){
			log.info("can't find child tree node by "+parent);
			return;
		}
		
		DefaultMutableTreeNode deletedChildNode = getFromStorage(deletedChild);
		if(deletedChildNode == null){
			log.info("can't find tree node by "+deletedChildNode);
			return;
		}
		
		removeChild(deletedChildNode,deletedChild);
		
	}
	
	private void removeChild(DefaultMutableTreeNode treeNode, Node deletedNode) {
		boolean selectParent = treeFacade.isSelectedNode(treeNode);
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)treeNode.getParent();
		treeFacade.removeNode(treeNode);
		removeFromStorage(deletedNode);
		if(parent != null){
			if(selectParent){
				treeFacade.setSelection(parent);
				tree.requestFocus();
			}
		}
	}
	
	@Override
	protected void onNodeUpdated(Node old, Node updatedNode, Collection<UpdateRequest> updates) {
		DefaultMutableTreeNode oldNode = getFromStorage(old);
		if(oldNode == null){
			log.info("can't find tree node by "+old);
			return;
		}
		
		updateNode(oldNode, old, updatedNode);
	}
	
	private void updateNode(DefaultMutableTreeNode treeNode, Node old, Node updatedNode) {
		treeNode.setUserObject(updatedNode);
		removeFromStorage(old);
		addToStorage(updatedNode, treeNode);
		treeFacade.refresh(treeNode);
	}
	
	@Override
	protected void onNodeMoved(Node oldParent, Node node, Node newParent) {
		DefaultMutableTreeNode oldNode = getFromStorage(node);
		if(oldNode == null){
			log.info("can't find tree node by "+node);
			return;
		}
		
		DefaultMutableTreeNode newParentNode = getFromStorage(newParent);
		if(newParentNode == null){
			log.info("can't find tree node by "+newParent);
			return;
		}
		
		treeFacade.moveNode(newParentNode, oldNode);
	}
	
	
	
	@EventListener
	public void onNodeChanged(NodeChanged event){
		Node node = event.node;
		DefaultMutableTreeNode treeNode = getFromStorage(node);
		if(treeNode != null){
			treeFacade.refresh(treeNode);
		}
	}

	@EventListener
	public void onNodeReverted(NodeReverted event){
		Node node = event.node;
		DefaultMutableTreeNode treeNode = getFromStorage(node);
		if(treeNode != null){
			treeFacade.refresh(treeNode);
		}
	}
	


	@EventListener
	public void onChildMoved(ChildMoved event){
		Node parent = event.parent;
		Node child = event.child;
		DefaultMutableTreeNode parentNode = getFromStorage(parent);
		DefaultMutableTreeNode childNode = getFromStorage(child);
		if(parentNode != null && childNode != null){
			int newIndex = event.newIndex;
			treeFacade.moveChild(childNode, newIndex);
		}
	}
	
	
	@EventListener
	public void onSelectRequest(SelectNodeRequest event){
		DefaultMutableTreeNode node = getFromStorage(event.node);
		if(node != null){
			treeFacade.setSelection(node);
		}
	}
	
	
	

	private void addToStorage(Node node, DefaultMutableTreeNode treeNode) {
		runtimeStorage.putWithWeakReferenceDomain(node, treeNodeKey, treeNode);
	}
	
	private DefaultMutableTreeNode getFromStorage(Node node){
		return runtimeStorage.get(node, treeNodeKey);
	}
	
	private DefaultMutableTreeNode removeFromStorage(Node node){
		return runtimeStorage.remove(node, treeNodeKey);
	}

	@Override
	public void setFocusRequest() {
		tree.requestFocus();
	}
	
	

}
