package ru.chapaj.util.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.chapaj.util.event.annotation.LastEventListener;
import ru.chapaj.util.lang.ClassUtil;

public class EventManager {
	
	static class ListenerObject<T extends Event<?>> {
		EventListener<T> eventListener;
		Method method;
		Object methodObj;
		EventCallback<?, T> eventCallback;
		
		public ListenerObject(EventCallback<?, T> eventCallback) {
			super();
			this.eventCallback = eventCallback;
		}

		public ListenerObject(EventListener<T> eventListener) {
			super();
			this.eventListener = eventListener;
		}

		public ListenerObject(Object methodObj,Method method) {
			super();
			this.method = method;
			this.methodObj = methodObj;
		}
		
	}
	
	static class ListenerList {
		ArrayList<ListenerObject<?>> unsorted = new ArrayList<ListenerObject<?>>();
		ArrayList<ListenerObject<?>> last = new ArrayList<ListenerObject<?>>();
	}
	
	private Map<Class<?>, ListenerList> listeners = new HashMap<Class<?>, ListenerList>();
	
	private ListenerExceptionHandler exceptionHandler;
	
	public void setExceptionHandler(ListenerExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public EventManager() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public void fireEvent(Object source, Event<?> event){
		ListenerList listeners = getListeners(event.getClass(), false);
		if(listeners != null){
			List<ListenerObject> toDeleteFromUnsorted = new ArrayList<ListenerObject>();
			List<ListenerObject> toDeleteFromLast = new ArrayList<ListenerObject>();
			try {
				for(ListenerObject listenerObject : listeners.unsorted){
					invokeListenerObject(source, event, listenerObject,toDeleteFromUnsorted);
				}
				
				for(ListenerObject listenerObject : listeners.last){
					invokeListenerObject(source, event, listenerObject,toDeleteFromLast);
				}
			}
			catch (StopEventException e) {
				//log
			}
			catch (Exception e) {
				if(exceptionHandler != null){
					exceptionHandler.handle(e);
				}
				else {
					throw new FireEventException(e);
				}
			}
			finally {
				for (ListenerObject listenerObject : toDeleteFromUnsorted) {
					listeners.unsorted.remove(listenerObject);
				}
				for (ListenerObject listenerObject : toDeleteFromLast) {
					listeners.last.remove(listenerObject);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void invokeListenerObject(Object source, Event<?> event,
			ListenerObject listenerObject,List<ListenerObject> toDelete) throws IllegalAccessException,
			InvocationTargetException {
		//это класс слушатель
		if(listenerObject.eventListener != null){
			listenerObject.eventListener.onAction(source, event);
		}
		//это метод в объекте помеченный аннотацией слушателя
		else if(listenerObject.method != null){
			Object ob = listenerObject.methodObj;
			Method method = listenerObject.method;
			method.setAccessible(true);
			Class<?>[] parameterTypes = method.getParameterTypes();
			if(parameterTypes == null || parameterTypes.length == 0){
				method.invoke(ob);
			}
			else if(parameterTypes.length == 1){
				Class<?> clazz = parameterTypes[0];
				if(ClassUtil.isValid(clazz, event.getClass())){
					method.invoke(ob, event);
				}
				else if(clazz.equals(Object.class)){
					method.invoke(ob, source);
				} else {
					throw new IllegalArgumentException("Cannot invoke "+method+". Unknow argument class: "+clazz+". Object or valid Event class expected");
				}
			}
			else if(parameterTypes.length == 2){
				Class<?> clazz1 = parameterTypes[0];
				Class<?> clazz2 = parameterTypes[1];
				if(!clazz1.equals(Object.class)){
					throw new IllegalArgumentException("Cannot invoke "+method+". Unknow argument class: "+clazz1+". Object class expected");
				}
				if(!ClassUtil.isValid(clazz2, event.getClass())){
					throw new IllegalArgumentException("Cannot invoke "+method+". Unknow argument class: "+clazz1+". Valid Event class expected");
				}
				method.invoke(ob, source,event);
			}
			else {
				throw new IllegalArgumentException("Cannot invoke "+method+". Invalid arguments count. 0,1,2 expected");
			}
			
		}
		//это событие-колбек
		else if(listenerObject.eventCallback != null){
			listenerObject.eventCallback.onAction(source, event);
			//удаляем кобек из подписчиков
			toDelete.add(listenerObject);
		}
	}

	public <T extends Event<?>> void addListener(EventListener<T> listener) {
		getListeners(listener.clazz,true).unsorted.add(new ListenerObject<T>(listener));
	}
	
	public <T extends Event<?>> void addLastListener(EventListener<T> listener) {
		getListeners(listener.clazz,true).last.add(new ListenerObject<T>(listener));
	}
	
	/**
	 * Добавить методы объекта в кандидаты вызова нужного события
	 * @param ob
	 */
	public void addObjectMethodListeners(Object ob){
		Method[] methods = ob.getClass().getDeclaredMethods();
		for (Method candidat : methods) {
			ru.chapaj.util.event.annotation.EventListener el = candidat.getAnnotation(ru.chapaj.util.event.annotation.EventListener.class);
			if(el != null){
				getListeners(el.value(), true).unsorted.add(new ListenerObject<Event<?>>(ob,candidat));
			}
			LastEventListener lel = candidat.getAnnotation(LastEventListener.class);
			if(lel != null){
				getListeners(lel.value(), true).last.add(new ListenerObject<Event<?>>(ob,candidat));
			}
		}
	}
	
	public <T extends Event<?>,C extends Event<?>> void fireEventCallback(Object source, EventCallback<T,C> eventCallback){
		getListeners(eventCallback.clazz,true).unsorted.add(new ListenerObject<C>(eventCallback));
		fireEvent(source, eventCallback.event);
	}
	
	public <T extends Event<?>,C extends Event<?>> void fireLastEventCallback(Object source, EventCallback<T,C> eventCallback){
		getListeners(eventCallback.clazz,true).last.add(new ListenerObject<C>(eventCallback));
		fireEvent(source, eventCallback.event);
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
