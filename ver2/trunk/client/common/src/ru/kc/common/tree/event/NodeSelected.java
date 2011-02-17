package ru.kc.common.tree.event;

import ru.kc.model.Node;
import ru.kc.platform.domain.annotation.DomainSpecific;
import ru.kc.platform.event.Event;

@DomainSpecific
public class NodeSelected extends Event {
	
	public final Node node;

	public NodeSelected(Node node) {
		this.node = node;
	}

}
