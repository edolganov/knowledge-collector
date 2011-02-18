package ru.kc.common.node.edit.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Event;

public class UpdateNodeRequest extends Event {
	
	public final Node node;

	public UpdateNodeRequest(Node node) {
		super();
		this.node = node;
	}

}
