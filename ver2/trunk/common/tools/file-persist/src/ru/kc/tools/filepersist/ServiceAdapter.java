package ru.kc.tools.filepersist;

import ru.kc.model.Node;

public class ServiceAdapter implements ServiceListener {

	@Override
	public void onAdded(Node parent, Node child) {}

	@Override
	public void onDeletedRecursive(Node parent, Node deletedChild) {}
	
	@Override
	public void onNodeUpdated(Node node) {}

}
