package ru.kc.module.tree.tools;

import javax.swing.JTree;

import ru.kc.main.common.Controller;
import ru.kc.module.tree.TreeController;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.keyboard.DeleteKey;

@Mapping(Tree.class)
public class TreeKeys extends Controller<Tree>{

	@Override
	public void init() {}
	
	@Override
	protected void afterAllInited() {
		JTree tree = ui.tree;
		final TreeController treeController = getController(TreeController.class);
		
		tree.addKeyListener(new DeleteKey() {
			
			@Override
			protected void doAction() {
				treeController.deleteNodeRequest();
			}
			
		});
	}

}
