package ru.kc.common.node.edit.event;

import ru.kc.model.Node;

public class TextChanged extends NodeChanged {
	
	public final String newText;

	public TextChanged(Node node, String newText) {
		super(node);
		this.newText = newText;
	}

}
