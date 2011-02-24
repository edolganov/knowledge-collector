package ru.kc.common.node.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class OpenNodeRequest extends Event {
	
	public final Node node;

	public OpenNodeRequest(Node node) {
		super();
		this.node = node;
	}
	

}
