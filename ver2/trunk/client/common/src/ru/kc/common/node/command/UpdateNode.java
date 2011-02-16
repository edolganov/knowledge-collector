package ru.kc.common.node.command;

import ru.kc.common.command.RollbackableCommand;
import ru.kc.model.Node;
import ru.kc.util.Check;

public class UpdateNode extends RollbackableCommand<Void>{
	
	Node node;
	String newName;
	String newDesctiption;
	
	public UpdateNode(Node node, String newName, String newDesctiption) {
		super();
		this.node = node;
		this.newName = newName;
		this.newDesctiption = newDesctiption;
	}

	@Override
	protected Void invoke() throws Exception {
		if(Check.isEmpty(newName))
			throw new IllegalArgumentException("name is empty");
		
		updater.update(node, newName, newDesctiption);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		//TODO
	}

}
