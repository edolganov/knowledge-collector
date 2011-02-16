package ru.kc.common.node.edit.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class NodeReverted extends Event {
	
	public final Node node;

	public NodeReverted(Node node) {
		super();
		this.node = node;
	}

}
