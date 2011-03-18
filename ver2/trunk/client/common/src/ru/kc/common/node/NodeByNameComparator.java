package ru.kc.common.node;

import java.util.Comparator;

import ru.kc.model.Node;

public class NodeByNameComparator implements Comparator<Node> {

	@Override
	public int compare(Node a, Node b) {
		return a.getName().compareTo(b.getName());
	}

}
