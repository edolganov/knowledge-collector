package ru.kc.platform.event;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.kc.platform.event.annotation.LastEventListener;



public class EventManager {
	
	private static class ListenerObject<T extends Event<?>> {
		EventListener<T> eventListener;
		Method method;
		WeakReference<Object> methodObj;
		EventCallback<?, T> eventCallback;
		String objectId;
		
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
			this.methodObj = new WeakReference<Object>(methodObj);
			this.objectId = methodObj.toString();
		}
		
	}
	
	static class ListenerList {
		List<ListenerObject<?>> unsorted = new CopyOnWriteArrayList<ListenerObject<?>>();
		List<ListenerObject<?>> last = new CopyOnWriteArrayList<ListenerObject<?>>();
		
		public boolean removeByMethod(Object ob) {
			boolean found = false;
			found = removeByMethod(unsorted, ob);
			if(!found) found = removeByMethod(last, ob);
			return found;
		}
		
		private boolean removeByMethod(List<ListenerObject<?>> list, Object ob) {
			boolean found = false;
			for (int i = 0; i < list.size(); i++) {
				ListenerObject<?> listenerObject = list.get(i);
				if(isGoal(listenerObject,ob)){
					list.remove(i);
					found = true;
				}
			}
			return found;
		}
		
		private boolean isGoal(ListenerObject<?> listenerObject, Object ob) {
			WeakReference<Object> ref = listenerObject.methodObj;
			if(ref != null){
				return 	ob == ref.get();
			} else {
				return false;
			}
		}
	}
	
	private Map<Class<?>, ListenerList> listeners = new HashMap<Class<?>, ListenerList>();
	
	private ListenerExceptionHandler exceptionHandler;
	
	public void setExceptionHandler(ListenerExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}



	public EventManager() {
		super();
	}
	
	@SuppressWarnings({ "rawtypes" })
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
					//System.out.println("EventManager: clean data for deleted object " + listenerObject.objectId);
					listeners.unsorted.remove(listenerObject);
				}
				for (ListenerObject listenerObject : toDeleteFromLast) {
					//System.out.println("EventManager: clean data for deleted object " + listenerObject.objectId);
					listeners.last.remove(listenerObject);
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void invokeListenerObject(Object source, Event<?> event,
			ListenerObject listenerObject,List<ListenerObject> toDelete) throws IllegalAccessException,
			InvocationTargetException {
		//это класс слушатель
		if(listenerObject.eventListener != null){
			listenerObject.eventListener.onAction(source, event);
		}
		//это метод в объекте помеченный аннотацией слушателя
		else if(listenerObject.method != null){
			WeakReference weakRef = listenerObject.methodObj;
			Object ob = weakRef.get();
			if(ob == null) {
				toDelete.add(listenerObject);
				return;
			}
			
			Method method = listenerObject.method;
			method.setAccessible(true);
			Class<?>[] parameterTypes = method.getParameterTypes();
			if(parameterTypes == null || parameterTypes.length == 0){
				method.invoke(ob);
			}
			else if(parameterTypes.length == 1){
				Class<?> clazz = parameterTypes[0];
				if(event.getClass().isAssignableFrom(clazz)){
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
				if(!event.getClass().isAssignableFrom(clazz2)){
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
	

	public void addObjectMethodListeners(Object ob){
		Class<?> scanningClazz = ob.getClass();
		while(Object.class != scanningClazz){
			Method[] methods = scanningClazz.getDeclaredMethods();
			for (Method candidat : methods) {
				ru.kc.platform.event.annotation.EventListener el = candidat.getAnnotation(ru.kc.platform.event.annotation.EventListener.class);
				if(el != null){
					Class<?> eventClass = findEventClass(candidat,el.value());
					if(eventClass != null){
						getListeners(eventClass, true).unsorted.add(new ListenerObject<Event<?>>(ob,candidat));
					}
				}
				LastEventListener lel = candidat.getAnnotation(LastEventListener.class);
				if(lel != null){
					Class<?> eventClass = findEventClass(candidat,lel.value());
					if(eventClass != null){
						getListeners(eventClass, true).last.add(new ListenerObject<Event<?>>(ob,candidat));
					}
				}
			}
			//next
			scanningClazz = scanningClazz.getSuperclass();
		}
	}
	
	public void removeObjectMethodListener(Object ob){
		boolean found = false;
		Class<?> scanningClazz = ob.getClass();
		while(Object.class != scanningClazz){
			Method[] methods = scanningClazz.getDeclaredMethods();
			for (Method candidat : methods) {
				ru.kc.platform.event.annotation.EventListener el = candidat.getAnnotation(ru.kc.platform.event.annotation.EventListener.class);
				if(el != null){
					Class<?> eventClass = findEventClass(candidat,el.value());
					if(eventClass != null){
						if(!found) found = getListeners(eventClass, true).removeByMethod(ob);
					}
				}
				LastEventListener lel = candidat.getAnnotation(LastEventListener.class);
				if(lel != null){
					Class<?> eventClass = findEventClass(candidat,lel.value());
					if(eventClass != null){
						if(!found) found = getListeners(eventClass, true).removeByMethod(ob);
					}
				}
			}
			//next
			scanningClazz = scanningClazz.getSuperclass();
		}
		
		if(found){
			//System.out.println("EventManager: unSubscribe " + ob);
		}
	}
	
	private Class<?> findEventClass(Method candidat, Class<?> classCandidat){
		if(classCandidat != null && classCandidat != Event.class){
			if(Event.class.isAssignableFrom(classCandidat)){
				return classCandidat;
			}
		}
		
		Class<?> eventClass = null;
		Class<?>[] types = candidat.getParameterTypes();
		if(types != null){
			for (Class<?> clazzCandidat : types) {
				if(Event.class.isAssignableFrom(clazzCandidat)){
					eventClass = clazzCandidat;
					break;
				}
			}
		}
		return eventClass;
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
