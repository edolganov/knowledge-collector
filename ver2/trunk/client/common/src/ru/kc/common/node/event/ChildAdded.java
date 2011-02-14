package ru.kc.common.node.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class ChildAdded extends Event{
	
	public final Node parent;
	public final Node child;
	
	public ChildAdded(Node parent, Node child) {
		super();
		this.parent = parent;
		this.child = child;
	}

}
