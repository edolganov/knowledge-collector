package ru.kc.common.node;

import ru.kc.model.Node;

public interface NodeContainerListener {
	
	void onModified(Node node);
	
	void onReverted(Node node);

}
