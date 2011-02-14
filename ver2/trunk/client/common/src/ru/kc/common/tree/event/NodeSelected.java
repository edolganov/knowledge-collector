package ru.kc.common.tree.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class NodeSelected extends Event {
	
	public final Node node;

	public NodeSelected(Node node) {
		super();
		this.node = node;
	}

}
