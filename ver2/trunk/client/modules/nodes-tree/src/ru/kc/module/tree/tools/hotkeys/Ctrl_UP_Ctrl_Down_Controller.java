package ru.kc.module.tree.tools.hotkeys;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.command.MoveDown;
import ru.kc.common.node.command.MoveUp;
import ru.kc.model.Node;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.component.ComponentUtil;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class Ctrl_UP_Ctrl_Down_Controller extends Controller<Tree> {
	
	
	TreeFacade treeFacade;
	
	@SuppressWarnings("serial")
	@Override
	protected void init() {
		
		treeFacade = new TreeFacade(ui.tree);
		
		ComponentUtil.addAction(ui.tree, "control UP", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				moveUp();
			}
		});
		
		ComponentUtil.addAction(ui.tree, "control DOWN", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown();
			}
		});
	}



	protected void moveUp() {
		DefaultMutableTreeNode treeNode = treeFacade.getCurrentNode();
		if(treeNode != null && treeNode.isRoot()) return;
		
		Node node = treeFacade.getCurrentObject(Node.class);
		if(node != null){
			invokeSafe(new MoveUp(node));
		}
	}

	protected void moveDown() {
		DefaultMutableTreeNode treeNode = treeFacade.getCurrentNode();
		if(treeNode != null && treeNode.isRoot()) return;
		
		Node node = treeFacade.getCurrentObject(Node.class);
		if(node != null){
			invokeSafe(new MoveDown(node));
		}
	}


}
