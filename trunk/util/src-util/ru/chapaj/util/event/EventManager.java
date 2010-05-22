package ru.chapaj.util.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventManager {
	
	static class ListenerList {
		ArrayList<EventListener<?>> unsorted = new ArrayList<EventListener<?>>();
		ArrayList<EventListener<?>> last = new ArrayList<EventListener<?>>();
	}
	
	private Map<Class<?>, ListenerList> listeners = new HashMap<Class<?>, ListenerList>();
	
	public EventManager() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public void fireEvent(Object source, Event<?> event){
		ListenerList listeners = getListeners(event.getClass(), false);
		if(listeners != null){
			try {
				for(EventListener eventListener : listeners.unsorted){
					eventListener.onAction(source, event);
				}
				for(EventListener eventListener : listeners.last){
					eventListener.onAction(source, event);
				}
			}
			catch (StopEventException e) {
				//log
			}
			catch (Exception e) {
				throw new FireEventException(e);
			}
		}
	}

	public void addListener(EventListener<Event<?>> listener) {
		getListeners(listener.clazz,true).unsorted.add(listener);
	}
	
	public <T extends Event<?>> void addLastListener(EventListener<T> listener) {
		getListeners(listener.clazz,true).last.add(listener);
	}
	
	
	private /*synchronized*//*if need*/ ListenerList getListeners(Class<?> clazz, boolean create){
		ListenerList listenerList = listeners.get(clazz);
		if(listenerList == null && create){
			listenerList = new ListenerList();
			listeners.put(clazz, listenerList);
		}
		return listenerList;
	}

}
