package ru.kc.common.node.command;

import ru.kc.common.command.RollbackableCommand;
import ru.kc.model.Node;

public class MoveUp extends RollbackableCommand<Void>{
	
	Node node;
	
	public MoveUp(Node node) {
		super();
		this.node = node;
	}


	@Override
	protected Void invoke() throws Exception {
		persistTree.moveUp(node);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		//TODO
	}

}
