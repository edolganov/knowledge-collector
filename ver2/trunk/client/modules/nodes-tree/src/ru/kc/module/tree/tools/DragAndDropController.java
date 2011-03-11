package ru.kc.module.tree.tools;

import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.command.MoveNode;
import ru.kc.model.Node;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class DragAndDropController extends Controller<Tree> implements TreeTransferHandler.Listener {
	
	TreeFacade treeFacade;
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		ui.tree.setTransferHandler(new TreeTransferHandler(this));
		ui.tree.setDragEnabled(true);
	}
	
	@Override
	public void onMove(DefaultMutableTreeNode destination, Object ob) {
		destination = getNotNullDestination(destination);
		Object userObject = destination.getUserObject();
		
		if(userObject instanceof Node){
			Node parent = (Node) userObject;
			if(ob instanceof Node){
				move((Node)ob, parent);
			} else {
				log.info("unknow type to move: "+ob);
			}
		} else {
			log.info("unknow destination: "+userObject);
		}
	}

	private void move(Node node, Node parent) {
		invokeSafe(new MoveNode(node, parent));
	}

	@Override
	public void onCopy(DefaultMutableTreeNode destination, Object ob) {
		destination = getNotNullDestination(destination);
		
		log.info("TODO copy");
	}
	
	private DefaultMutableTreeNode getNotNullDestination(DefaultMutableTreeNode destination) {
		if(destination == null) destination = treeFacade.getRoot();
		return destination;
	}

}
