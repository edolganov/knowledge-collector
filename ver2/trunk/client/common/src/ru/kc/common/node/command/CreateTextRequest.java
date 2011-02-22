package ru.kc.common.node.command;

import ru.kc.common.command.Command;
import ru.kc.common.node.create.CreateTextModule;
import ru.kc.model.Node;

public class CreateTextRequest extends Command<Void>{
	
	Node parent;

	public CreateTextRequest(Node parent) {
		super();
		this.parent = parent;
	}

	@Override
	protected Void invoke() throws Exception {
		if(parent == null) return null;
		
		CreateTextModule module = new CreateTextModule();
		module.createAndShow(rootUI, true);
		Node child = module.getText();
		if(child != null){
			invokeSafe(new AddChild(parent, child));
		}
		return null;
	}

}
