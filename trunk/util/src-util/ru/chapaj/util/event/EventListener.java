package ru.chapaj.util.event;


public abstract class EventListener<T extends Event<?>> {
	
	Class<T> clazz;
	
	public EventListener(Class<T> clazz){
		this.clazz = clazz;
	}
	
	public abstract void onAction(Object source, T event) throws StopEventException;
	

}
