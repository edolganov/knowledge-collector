package ru.kc.common.node.command;

import ru.kc.common.command.RollbackableCommand;
import ru.kc.model.Node;

public class MoveDown extends RollbackableCommand<Void>{
	
	Node node;
	
	public MoveDown(Node node) {
		super();
		this.node = node;
	}


	@Override
	protected Void invoke() throws Exception {
		persistTree.moveDown(node);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		//TODO
	}

}
