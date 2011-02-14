package ru.kc.main.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class NodeUpdated extends Event<Void>{
	
	public final Node node;
	
	public NodeUpdated(Node node) {
		super();
		this.node = node;
	}

}
