package ru.chapaj.util.event;

public abstract class EventCallback<T extends Event<?>,C extends Event<?>>{
	
	T event;
	Class<C> clazz;
	
	public EventCallback(T event, Class<C> clazz){
		this.event = event;
		this.clazz = clazz;
	}
	
	public abstract void onAction(Object source, C event) throws StopEventException;

}
