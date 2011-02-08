package ru.kc.main.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class ChildAdded extends Event<Void>{
	
	public final Node parent;
	public final Node child;
	
	public ChildAdded(Node parent, Node child) {
		super();
		this.parent = parent;
		this.child = child;
	}

}
