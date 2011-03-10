package ru.kc.common.node.command;

import ru.kc.common.command.Command;
import ru.kc.common.node.create.CreateFileLinkModule;
import ru.kc.model.Node;

public class CreateFileLinkRequest extends Command<Void>{
	
	Node parent;

	public CreateFileLinkRequest(Node parent) {
		super();
		this.parent = parent;
	}

	@Override
	protected Void invoke() throws Exception {
		if(parent == null) return null;
		
		CreateFileLinkModule module = new CreateFileLinkModule();
		module.createAndShow(rootUI, true);
		Node child = module.getNode();
		if(child != null){
			invokeSafe(new AddChild(parent, child));
		}
		return null;
	}

}
