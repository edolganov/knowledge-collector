package ru.kc.module.search.tools;

import java.util.List;

import ru.kc.model.Node;

public class Group {
	
	public final Class<?> type;
	public final List<Node> nodes;
	
	public Group(Class<?> type, List<Node> nodes) {
		super();
		this.type = type;
		this.nodes = nodes;
	}
	
}