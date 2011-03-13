package ru.kc.tools.filepersist.impl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.ServiceListener;
import ru.kc.tools.filepersist.update.UpdateRequest;

public class Listeners {
	
	private CopyOnWriteArrayList<ServiceListener> listeners = new CopyOnWriteArrayList<ServiceListener>();
	
	public void addListener(ServiceListener listener) {
		listeners.add(listener);
	}
	
	public void fireAddedEvent(Node parent, Node child) {
		for(ServiceListener l : listeners) l.onAdded(parent, child);
	}

	public void fireDeletedEvent(Node parent, Node child, List<Node> deletedSubChildren) {
		for(ServiceListener l : listeners) l.onDeletedRecursive(parent, child, deletedSubChildren);
	}
	
	public void fireUpdatedEvent(Node old, Node updated, Collection<UpdateRequest> updates) {
		for(ServiceListener l : listeners) l.onNodeUpdated(old, updated, updates);
	}
	
	public void fireMovedEvent(Node oldParent, Node child, Node newParent) {
		for(ServiceListener l : listeners) l.onMoved(oldParent, child, newParent);
	}
	
	public void fireMovedChildEvent(Node parent, Node child, int newIndex){
		for(ServiceListener l : listeners) l.onChildMoved(parent, child, newIndex);
	}

}
