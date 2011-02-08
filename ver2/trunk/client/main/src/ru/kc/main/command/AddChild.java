package ru.kc.main.command;

import ru.kc.main.RollbackableCommand;
import ru.kc.model.Node;

public class AddChild extends RollbackableCommand<Void> {
	
	Node parent;
	Node child;

	public AddChild(Node parent, Node child) {
		super();
		this.parent = parent;
		this.child = child;
	}

	@Override
	protected Void invoke() throws Exception {
		persistTree.add(parent, child);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		//TODO
	}

}
