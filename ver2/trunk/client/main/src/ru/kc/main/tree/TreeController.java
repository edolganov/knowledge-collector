package ru.kc.main.tree;


import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import ru.kc.main.Controller;
import ru.kc.main.tree.tools.CellRender;
import ru.kc.main.tree.ui.Tree;
import ru.kc.model.Node;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.tree.TreeTransferHandler;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class TreeController extends Controller<Tree>{
	
	JTree tree;
	TreeFacade treeFacade;
	DefaultTreeModel model;

	@Override
	public void init() {
		tree = ui.tree;
		treeFacade = new TreeFacade(ui.tree);
		
		//tree.setRootVisible(false);
		tree.setTransferHandler(new TreeTransferHandler());
		tree.setDragEnabled(true);
		tree.setModel(TreeFacade.createDefaultModel(TreeFacade.createNode("")));
		tree.setCellRenderer(new CellRender());
		treeFacade.setSingleSelection();
		
		
		buildTree();
	}

	private void buildTree() {
		try {
			Node rootNode = persistTree.getRoot();
			model = TreeFacade.createDefaultModel(rootNode);
			tree.setModel(model);
			
			LinkedList<DefaultMutableTreeNode> queue = new LinkedList<DefaultMutableTreeNode>();
			queue.addLast(treeFacade.getRoot());
			while(queue.size() > 0){
				DefaultMutableTreeNode treeNode = queue.removeFirst();
				Node node = (Node)treeNode.getUserObject();
				List<Node> children = node.getChildren();
				for(Node child : children){
					DefaultMutableTreeNode treeChild = treeFacade.addChild(treeNode, child);
					queue.addLast(treeChild);
				}
			}
			tree.revalidate();
			tree.repaint();
		}catch (Exception e) {
			log.error("error init tree", e);
		}
	}

}
