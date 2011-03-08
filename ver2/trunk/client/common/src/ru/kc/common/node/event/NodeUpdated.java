package ru.kc.common.node.event;

import java.util.Collection;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;
import ru.kc.tools.filepersist.update.UpdateRequest;

public class NodeUpdated extends Event{
	
	public final Node old;
	public final Node node;
	public final Collection<UpdateRequest> updates;

	public NodeUpdated(Node old, Node node, Collection<UpdateRequest> updates) {
		super();
		this.old = old;
		this.node = node;
		this.updates = updates;
	}
	


}
