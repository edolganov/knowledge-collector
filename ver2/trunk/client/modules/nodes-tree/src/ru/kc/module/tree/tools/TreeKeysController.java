package ru.kc.module.tree.tools;

import java.awt.event.KeyEvent;

import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.command.DeleteNode;
import ru.kc.model.Node;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.keyboard.DeleteKey;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class TreeKeysController extends Controller<Tree>{

	TreeFacade treeFacade;
	
	@Override
	public void init() {
		treeFacade = new TreeFacade(ui.tree);
		
		ui.tree.addKeyListener(new DeleteKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				deleteNodeRequest();
			}
			
		});
	}
	
	public void deleteNodeRequest(){
		DefaultMutableTreeNode treeNode = treeFacade.getCurrentNode();
		if(treeNode.isRoot()) return;
		
		Node node = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new DeleteNode(node));
	}

}
