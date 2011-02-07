package ru.kc.main.tree;


import javax.swing.JTree;

import ru.kc.main.Controller;
import ru.kc.main.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.tree.TreeTransferHandler;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class TreeController extends Controller<Tree>{
	
	JTree tree;
	TreeFacade treeFacade;

	@Override
	public void init() {
		tree = ui.tree;
		treeFacade = new TreeFacade(ui.tree);
		
		tree.setTransferHandler(new TreeTransferHandler());
		tree.setDragEnabled(true);
		tree.setModel(TreeFacade.createDefaultModel(TreeFacade.createNode("")));
		treeFacade.setSingleSelection();
		
		fillTree();
	}

	private void fillTree() {
		System.out.println("!!!! "+context);
	}

}
