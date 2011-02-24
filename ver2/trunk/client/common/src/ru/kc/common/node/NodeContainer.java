package ru.kc.common.node;

import ru.kc.model.Node;

public interface NodeContainer<T extends Node> {
	
	void setNode(T node);
	
	T getNode();

}
