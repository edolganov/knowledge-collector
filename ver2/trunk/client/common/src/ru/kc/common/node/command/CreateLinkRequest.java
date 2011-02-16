package ru.kc.common.node.command;

import ru.kc.common.command.Command;
import ru.kc.common.node.create.CreateLinkModule;
import ru.kc.model.Node;

public class CreateLinkRequest extends Command<Void>{
	
	Node parent;

	public CreateLinkRequest(Node parent) {
		super();
		this.parent = parent;
	}

	@Override
	protected Void invoke() throws Exception {
		if(parent == null) return null;
		
		CreateLinkModule module = new CreateLinkModule();
		module.createAndShow(rootUI, true);
		Node child = module.getLink();
		if(child != null){
			invokeSafe(new AddChild(parent, child));
		}
		return null;
	}

}
