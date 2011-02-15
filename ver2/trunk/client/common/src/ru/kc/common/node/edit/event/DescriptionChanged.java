package ru.kc.common.node.edit.event;

import ru.kc.model.Node;

public class DescriptionChanged extends NodeChanged {
	
	public final String newDescription;

	public DescriptionChanged(Node node, String newDescription) {
		super(node);
		this.newDescription = newDescription;
	}

}
