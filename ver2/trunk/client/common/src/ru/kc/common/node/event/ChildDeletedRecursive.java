package ru.kc.common.node.event;

import java.util.List;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class ChildDeletedRecursive extends Event{
	
	public final Node parent;
	public final Node deletedChild;
	public final List<Node> deletedSubChildren;
	
	public ChildDeletedRecursive(Node parent, Node deletedChild,
			List<Node> deletedSubChildren) {
		super();
		this.parent = parent;
		this.deletedChild = deletedChild;
		this.deletedSubChildren = deletedSubChildren;
	}
	


}
