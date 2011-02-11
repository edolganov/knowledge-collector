package ru.kc.module.tree;


import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ru.kc.main.command.CreateDirRequest;
import ru.kc.main.command.basic.DeleteNode;
import ru.kc.main.common.Controller;
import ru.kc.model.Node;
import ru.kc.module.tree.tools.CellEditor;
import ru.kc.module.tree.tools.CellRender;
import ru.kc.module.tree.tools.TreeMenu;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.ExportAction;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.tree.MenuController;
import ru.kc.util.swing.tree.TreeFacade;
import ru.kc.util.swing.tree.TreeTransferHandler;

@Mapping(Tree.class)
public class TreeController extends Controller<Tree>{
	
	private static final String TREE_NODE_KEY = "tree-node-key";
	
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
		tree.setCellRenderer(new CellRender(tree));
		final CellEditor cellEditor = new CellEditor(tree);
		cellEditor.addCustomListener(new CellEditorListener() {
			
			@Override
			public void editingStopped(ChangeEvent e) {
				System.out.println("save!");
			}
			
			@Override
			public void editingCanceled(ChangeEvent e) {}
		});
		tree.setCellEditor(cellEditor);
		tree.setRowHeight(0);
		tree.setEditable(true);
		treeFacade.setSingleSelection();
		treeFacade.setPopupMenu(new TreeMenu(tree, appContext.commandService), new MenuController() {
			
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



	@ExportAction(description="create dir", icon="/ru/kc/main/img/createDir.png")
	public void createDirRequest(){
		Node parent = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new CreateDirRequest(parent));
	}
	
	@ExportAction(description="create link", icon="/ru/kc/main/img/createLink.png")
	public void createLinkRequest(){
		System.out.println("create link");
	}
	
	@ExportAction(description="create text", icon="/ru/kc/main/img/createText.png")
	public void createTextRequest(){
		System.out.println("create text");
	}
	
	@ExportAction(description="create file link", icon="/ru/kc/main/img/createFileLink.png")
	public void createFileLinkRequest(){
		System.out.println("create file link");
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
			log.error("can't find parent tree node by "+parent);
			return;
		}
		addChildToTree(parentNode, child);
	}
	
	@Override
	protected void onChildDeletedRecursive(Node parent, Node deletedChild) {
		DefaultMutableTreeNode parentNode = getFromStorage(parent);
		if(parentNode == null){
			log.error("can't find child tree node by "+parent);
			return;
		}
		
		DefaultMutableTreeNode deletedChildNode = getFromStorage(deletedChild);
		if(deletedChildNode == null){
			log.error("can't find tree node by "+deletedChildNode);
			return;
		}
		
		removeChild(deletedChildNode,deletedChild);
		
	}
	


	private DefaultMutableTreeNode addChildToTree(DefaultMutableTreeNode parentTreeNode, Node child) {
		DefaultMutableTreeNode treeChild = treeFacade.addChild(parentTreeNode, child);
		addToStorage(child,treeChild);
		treeFacade.setSelection(treeChild);
		tree.requestFocus();

		return treeChild;
	}
	
	private void removeChild(DefaultMutableTreeNode treeNode, Node deletedNode) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)treeNode.getParent();
		treeFacade.removeNode(treeNode);
		removeFromStorage(deletedNode);
		if(parent != null){
			treeFacade.setSelection(parent);
			tree.requestFocus();
		}
	}
	
	private void addToStorage(Node node, DefaultMutableTreeNode treeNode) {
		runtimeStorage.putWithWeakReferenceDomain(node, TREE_NODE_KEY, treeNode);
	}
	
	private DefaultMutableTreeNode getFromStorage(Node node){
		return runtimeStorage.get(node, TREE_NODE_KEY);
	}
	
	private DefaultMutableTreeNode removeFromStorage(Node node){
		return runtimeStorage.remove(node, TREE_NODE_KEY);
	}
	

}
