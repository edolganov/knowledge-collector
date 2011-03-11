package ru.kc.tools.filepersist;

import java.util.Collection;
import java.util.List;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.update.UpdateRequest;

public class ServiceAdapter implements ServiceListener {

	@Override
	public void onAdded(Node parent, Node child) {}

	@Override
	public void onDeletedRecursive(Node parent, Node deletedChild, List<Node> deletedSubChildren) {}
	
	@Override
	public void onNodeUpdated(Node old, Node updated, Collection<UpdateRequest> updates) {}

	@Override
	public void onMoved(Node oldParent, Node child, Node newParent) {}

}
