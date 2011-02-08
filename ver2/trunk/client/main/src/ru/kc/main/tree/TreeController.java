package ru.kc.main.tree;


import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ru.kc.main.command.AddChild;
import ru.kc.main.command.DeleteNode;
import ru.kc.main.common.Controller;
import ru.kc.main.tree.tools.CellRender;
import ru.kc.main.tree.tools.TreeMenu;
import ru.kc.main.tree.ui.Tree;
import ru.kc.model.Node;
import ru.kc.platform.annotations.ExportAction;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.keyboard.DeleteKey;
import ru.kc.util.swing.tree.TreeTransferHandler;
import ru.kc.util.swing.tree.TreeFacade;

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
		tree.setCellRenderer(new CellRender());
		treeFacade.setSingleSelection();
		treeFacade.setPopupMenu(new TreeMenu(tree, appContext.commandService));
		tree.addKeyListener(new DeleteKey() {
			
			@Override
			protected void doAction() {
				deleteNodeRequest();
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
		if(parent == null) return;
		
		//TODO create object by dialog
		Node child = persistFactory.createDir("test-"+System.currentTimeMillis(), null);
		invokeSafe(new AddChild(parent, child));
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
		Node node = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new DeleteNode(node));
	}
	
	@Override
	protected void onChildAdded(Node parent, Node child) {
		DefaultMutableTreeNode parentNode = getFromStorage(parent);
		if(parentNode == null){
			log.error("can't find tree node by "+parent);
			return;
		}
		addChildToTree(parentNode, child);
	}
	
	
	
	private DefaultMutableTreeNode addChildToTree(DefaultMutableTreeNode parentTreeNode, Node child) {
		DefaultMutableTreeNode treeChild = treeFacade.addChild(parentTreeNode, child);
		addToStorage(child,treeChild);
		return treeChild;
	}
	
	
	private void addToStorage(Node node, DefaultMutableTreeNode treeNode) {
		runtimeStorage.putWithWeakReferenceDomain(node, TREE_NODE_KEY, treeNode);
	}
	
	private DefaultMutableTreeNode getFromStorage(Node node){
		return runtimeStorage.get(node, TREE_NODE_KEY);
	}
	

}
