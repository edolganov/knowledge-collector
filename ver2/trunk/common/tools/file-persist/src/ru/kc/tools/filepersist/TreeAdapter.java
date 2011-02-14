package ru.kc.tools.filepersist;

import ru.kc.model.Node;

public class TreeAdapter implements ServiceListener {

	@Override
	public void onAdded(Node parent, Node child) {}

	@Override
	public void onDeletedRecursive(Node parent, Node deletedChild) {}

}
