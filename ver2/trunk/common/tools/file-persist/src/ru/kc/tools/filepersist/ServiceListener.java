package ru.kc.tools.filepersist;

import java.util.List;

import ru.kc.model.Node;

public interface ServiceListener {
	
	void onAdded(Node parent, Node child);

	void onDeletedRecursive(Node parent, Node deletedChild, List<Node> deletedSubChildren);
	
	void onNodeUpdated(Node old, Node updated);

}
