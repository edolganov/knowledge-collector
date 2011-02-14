package ru.kc.main.command.basic;

import ru.kc.common.command.RollbackableCommand;
import ru.kc.model.Node;
import ru.kc.util.Check;

public class RenameNode extends RollbackableCommand<Void> {
	
	Node node;
	String newName;

	

	public RenameNode(Node node, String newName) {
		super();
		this.node = node;
		this.newName = newName;
	}

	@Override
	protected Void invoke() throws Exception {
		if(Check.isEmpty(newName)){
			
			throw new IllegalArgumentException("name is empty");
		}
		updater.updateName(node, newName);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		//TODO
	}

}
