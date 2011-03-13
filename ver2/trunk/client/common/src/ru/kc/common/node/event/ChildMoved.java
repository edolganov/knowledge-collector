package ru.kc.common.node.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class ChildMoved extends Event{
	
	public final Node parent;
	public final Node child;
	public final int newIndex;
	
	
	public ChildMoved(Node parent, Node child, int newIndex) {
		super();
		this.parent = parent;
		this.child = child;
		this.newIndex = newIndex;
	}
	


	
	


}
