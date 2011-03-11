package ru.kc.common.node.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class NodeMoved extends Event{
	
	public final Node oldParent;
	public final Node node;
	public final Node newParent;
	
	public NodeMoved(Node oldParent, Node node, Node newParent) {
		super();
		this.oldParent = oldParent;
		this.node = node;
		this.newParent = newParent;
	}

	
	


}
