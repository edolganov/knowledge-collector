package ru.kc.common.node.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class NodeUpdated extends Event{
	
	public final Node old;
	public final Node node;
	
	public NodeUpdated(Node old, Node node) {
		super();
		this.old = old;
		this.node = node;
	}
	


}
