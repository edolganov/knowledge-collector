package ru.kc.common.node.edit.event;

import ru.kc.model.Node;

public class NameChanged extends NodeChanged {
	
	public final String newName;

	public NameChanged(Node node, String newName) {
		super(node);
		this.newName = newName;
	}

}
