package ru.kc.common.node.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class ChildDeletedRecursive extends Event<Void>{
	
	public final Node parent;
	public final Node deletedChild;
	
	public ChildDeletedRecursive(Node parent, Node deletedChild) {
		super();
		this.parent = parent;
		this.deletedChild = deletedChild;
	}

}
