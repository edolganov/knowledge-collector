package ru.kc.common.node.edit.event;

import ru.kc.model.Node;

public class UrlChanged extends NodeChanged {
	
	public final String newUrl;

	public UrlChanged(Node node, String newUrl) {
		super(node);
		this.newUrl = newUrl;
	}

}
