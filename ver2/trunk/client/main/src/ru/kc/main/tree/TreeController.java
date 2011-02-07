package ru.kc.main.tree;


import javax.swing.JTree;
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
		
		
		fillTree();
	}

	private void fillTree() {
		try {
			Node rootNode = persistTree.getRoot();
			model = TreeFacade.createDefaultModel(rootNode);
			tree.setModel(model);
		}catch (Exception e) {
			log.error("error init tree", e);
		}
		
	}

}
