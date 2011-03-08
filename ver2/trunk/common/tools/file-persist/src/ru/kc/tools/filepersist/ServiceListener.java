package ru.kc.tools.filepersist;

import java.util.Collection;
import java.util.List;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.update.UpdateRequest;

public interface ServiceListener {
	
	void onAdded(Node parent, Node child);

	void onDeletedRecursive(Node parent, Node deletedChild, List<Node> deletedSubChildren);
	
	void onNodeUpdated(Node old, Node updated, Collection<UpdateRequest> updates);

}
