package ru.kc.main.tree;

import javax.swing.JTree;


import ru.kc.main.tree.tools.TreeTransferHandler;
import ru.kc.main.tree.ui.Tree;
import ru.kc.platform.controller.Controller;
import ru.kc.platform.controller.annotations.Mapping;

@Mapping(Tree.class)
public class TreeController extends Controller<Tree>{
	

	@Override
	public void init() {
		JTree tree = ui.tree;
		tree.setTransferHandler(new TreeTransferHandler());
		tree.setDragEnabled(true);
		
	}

}
