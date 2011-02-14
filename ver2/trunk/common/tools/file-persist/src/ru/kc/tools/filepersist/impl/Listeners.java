package ru.kc.tools.filepersist.impl;

import java.util.concurrent.CopyOnWriteArrayList;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.ServiceListener;

public class Listeners {
	
	private CopyOnWriteArrayList<ServiceListener> listeners = new CopyOnWriteArrayList<ServiceListener>();
	
	public void addListener(ServiceListener listener) {
		listeners.add(listener);
	}
	
	public void fireAddedEvent(Node parent, Node child) {
		for(ServiceListener l : listeners) l.onAdded(parent, child);
	}

	public void fireDeletedEvent(Node parent, Node child) {
		for(ServiceListener l : listeners) l.onDeletedRecursive(parent, child);
	}
	
	public void fireUpdatedEvent(Node old, Node updated) {
		for(ServiceListener l : listeners) l.onNodeUpdated(old, updated);
	}

}
