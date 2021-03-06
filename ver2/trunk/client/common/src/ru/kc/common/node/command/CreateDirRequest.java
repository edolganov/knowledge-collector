package ru.kc.common.node.command;

import ru.kc.common.command.Command;
import ru.kc.common.node.create.CreateDirModule;
import ru.kc.model.Node;

public class CreateDirRequest extends Command<Void>{
	
	Node parent;

	public CreateDirRequest(Node parent) {
		super();
		this.parent = parent;
	}

	@Override
	protected Void invoke() throws Exception {
		if(parent == null) return null;
		
		CreateDirModule module = new CreateDirModule();
		module.createAndShow(rootUI, true);
		Node child = module.getDir();
		if(child != null){
			invokeSafe(new AddChild(parent, child));
		}
		return null;
	}

}
