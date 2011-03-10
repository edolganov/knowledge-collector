package ru.kc.module.tree;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.command.CreateDirRequest;
import ru.kc.common.node.command.CreateFileLinkRequest;
import ru.kc.common.node.command.CreateLinkRequest;
import ru.kc.common.node.command.CreateTextRequest;
import ru.kc.common.node.command.DeleteNode;
import ru.kc.common.node.command.UpdateNode;
import ru.kc.common.node.edit.event.NodeChanged;
import ru.kc.common.node.edit.event.NodeReverted;
import ru.kc.model.Node;
import ru.kc.module.tree.tools.CellEditor;
import ru.kc.module.tree.tools.CellRender;
import ru.kc.module.tree.tools.TreeMenu;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.ExportAction;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.update.UpdateName;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.util.swing.tree.MenuController;
import ru.kc.util.swing.tree.TreeFacade;
import ru.kc.util.swing.tree.TreeTransferHandler;

@Mapping(Tree.class)
public class TreeController extends Controller<Tree>{
	
	private final String treeNodeKey = "tree-node-key-"+hashCode();
	
	JTree tree;
	TreeFacade treeFacade;
	DefaultTreeModel model;

	@Override
	public void init() {
		tree = ui.tree;
		treeFacade = new TreeFacade(ui.tree);
		
		tree.setRootVisible(true);
		tree.setTransferHandler(new TreeTransferHandler());
		tree.setDragEnabled(true);
		tree.setModel(TreeFacade.createDefaultModelByUserObject(TreeFacade.createNode("")));
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
		treeFacade.setPopupMenu(new TreeMenu(tree, appContext, context), new MenuController() {
			
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
			model = TreeFacade.createDefaultModelByNode(treeRootNode);
			tree.setModel(model);

			
			
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



	@ExportAction(description="create dir", icon="/ru/kc/common/img/createDir.png")
	public void createDirRequest(){
		Node parent = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new CreateDirRequest(parent));
	}
	
	@ExportAction(description="create link", icon="/ru/kc/common/img/createLink.png")
	public void createLinkRequest(){
		Node parent = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new CreateLinkRequest(parent));
	}
	
	@ExportAction(description="create text", icon="/ru/kc/common/img/createText.png")
	public void createTextRequest(){
		Node parent = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new CreateTextRequest(parent));
	}
	
	@ExportAction(description="create file link", icon="/ru/kc/common/img/createFileLink.png")
	public void createFileLinkRequest(){
		Node parent = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new CreateFileLinkRequest(parent));
	}
	
	public void deleteNodeRequest(){
		DefaultMutableTreeNode treeNode = treeFacade.getCurrentNode();
		if(treeNode.isRoot()) return;
		
		Node node = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new DeleteNode(node));
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
	
	@Override
	protected void onNodeUpdated(Node old, Node updatedNode, Collection<UpdateRequest> updates) {
		DefaultMutableTreeNode oldNode = getFromStorage(old);
		if(oldNode == null){
			log.info("can't find tree node by "+old);
			return;
		}
		
		updateNode(oldNode, old, updatedNode);
	}
	
	@EventListener
	public void onNodeChanged(NodeChanged event){
		Node node = event.node;
		DefaultMutableTreeNode treeNode = getFromStorage(node);
		if(treeNode != null){
			treeFacade.reload(treeNode);
		}
	}
	
	@EventListener
	public void onNodeReverted(NodeReverted event){
		Node node = event.node;
		DefaultMutableTreeNode treeNode = getFromStorage(node);
		if(treeNode != null){
			treeFacade.reload(treeNode);
		}
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
	
	private void updateNode(DefaultMutableTreeNode treeNode, Node old, Node updatedNode) {
		treeNode.setUserObject(updatedNode);
		removeFromStorage(old);
		addToStorage(updatedNode, treeNode);
		treeFacade.reload(treeNode);
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
	

}
