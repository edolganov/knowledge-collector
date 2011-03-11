package ru.kc.common.node.command;

import ru.kc.common.command.RollbackableCommand;
import ru.kc.model.Node;

public class MoveNode extends RollbackableCommand<Void>{
	
	Node node;
	Node newParent;
	
	public MoveNode(Node node, Node newParent) {
		super();
		this.node = node;
		this.newParent = newParent;
	}


	@Override
	protected Void invoke() throws Exception {
		if(persistTree.canMove(node, newParent)){
			persistTree.move(node, newParent);
		}
		return null;
	}

	@Override
	public void rollback() throws Exception {
		//TODO
	}

}
