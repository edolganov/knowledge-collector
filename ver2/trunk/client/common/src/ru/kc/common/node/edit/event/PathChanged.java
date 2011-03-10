package ru.kc.common.node.edit.event;

import ru.kc.model.Node;

public class PathChanged extends NodeChanged {
	
	public final String newPath;

	public PathChanged(Node node, String newPath) {
		super(node);
		this.newPath = newPath;
	}

}
