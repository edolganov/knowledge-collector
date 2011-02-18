package ru.kc.common.node.command;

import java.util.Arrays;
import java.util.Collection;

import ru.kc.common.command.RollbackableCommand;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.update.UpdateRequest;

public class UpdateNode extends RollbackableCommand<Void>{
	
	Node node;
	Collection<UpdateRequest> updates;
	
	public UpdateNode(Node node, Collection<UpdateRequest> updates) {
		super();
		this.node = node;
		this.updates = updates;
	}
	
	public UpdateNode(Node node, UpdateRequest... updates) {
		super();
		this.node = node;
		this.updates = Arrays.asList(updates);
	}

	@Override
	protected Void invoke() throws Exception {
		updater.update(node, updates);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		//TODO
	}

}
