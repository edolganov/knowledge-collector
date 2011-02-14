package ru.kc.tools.filepersist;

import ru.kc.model.Node;

public interface ServiceListener {
	
	void onAdded(Node parent, Node child);

	void onDeletedRecursive(Node parent, Node deletedChild);
	
	void onNodeUpdated(Node node);

}
