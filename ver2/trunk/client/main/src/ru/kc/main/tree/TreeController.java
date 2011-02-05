package ru.kc.main.tree;

import javax.swing.TransferHandler;

import org.jdesktop.swingx.JXTree;

import ru.kc.main.tree.ui.Tree;
import ru.kc.platform.controller.Controller;
import ru.kc.platform.controller.annotations.Mapping;

@Mapping(Tree.class)
public class TreeController extends Controller<Tree>{
	

	@Override
	public void init() {
		JXTree tree = ui.tree;
		//tree.setTransferHandler(new TransferHandler(null));
		tree.setDragEnabled(true);
		
	}

}
