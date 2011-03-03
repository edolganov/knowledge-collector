package ru.kc.platform.event.model;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.event.Event;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.platform.event.annotation.FirstEventListener;
import ru.kc.platform.event.annotation.LastEventListener;

public class MethodListener {
	
	private Class<? extends Event> eventType;
	private WeakReference<Object> listenerObjectRef;
	private Method method;
	private ArrayList<WeakReference<Object>> domainKeys = new ArrayList<WeakReference<Object>>();
	
	private MethodListener(Class<? extends Event> eventType,
			Object ob, Method method) {
		super();
		this.eventType = eventType;
		this.method = method;
		listenerObjectRef = new WeakReference<Object>(ob);
		initDomainKeys(ob);

		
	}

	private void initDomainKeys(Object ob) {
		if(ob instanceof DomainMember){
			Object domainKey = ((DomainMember)ob).getDomainKey();
			domainKeys.add(new WeakReference<Object>(domainKey));
		}
		addRootDomainKeyIfNeed();
	}

	private void addRootDomainKeyIfNeed() {
		for(WeakReference<Object> ref : domainKeys){
			if(DomainMember.ROOT_DOMAIN_KEY.equals(ref.get())){
				return;
			}
		}
		domainKeys.add(new WeakReference<Object>(DomainMember.ROOT_DOMAIN_KEY));
	}



	public Class<?> getEventType() {
		return eventType;
	}
	
	public boolean isFirst() {
		return method.getAnnotation(FirstEventListener.class) != null;
	}
	
	public boolean isLast() {
		return method.getAnnotation(LastEventListener.class) != null;
	}
	
	public boolean like(MethodListener listener) {
		Object ob = listenerObjectRef.get();
		if(ob != null){
			Object otherOb = listener.listenerObjectRef.get();
			if(ob.equals(otherOb)){
				if(method.equals(listener.method)){
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValid() {
		return listenerObjectRef.get() != null;
	}
	
	
	public boolean belongsTo(Object domianKey) {
		Iterator<WeakReference<Object>> it = domainKeys.iterator();
		while(it.hasNext()){
			WeakReference<Object> ref = it.next();
			Object key = ref.get();
			if(key == null){
				it.remove();
			} else {
				if(key.equals(domianKey)) return true;
			}
		}
		
		return false;
	}
	
	public void process(Event event) throws Throwable {
		Object sender = event.getSender();
		Object ob = listenerObjectRef.get();
		if(ob == null) throw new IllegalStateException(this+" is incalid for process event");
		method.setAccessible(true);
		
		try {
			Class<?>[] parameterTypes = method.getParameterTypes();
			if(parameterTypes == null || parameterTypes.length == 0){
				method.invoke(ob);
			}
			else if(parameterTypes.length == 1){
				Class<?> clazz = parameterTypes[0];
				if(clazz.isAssignableFrom(event.getClass())){
					method.invoke(ob, event);
				}
				else if(clazz.equals(Object.class)){
					method.invoke(ob, sender);
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
				method.invoke(ob, sender, event);
			}
			else {
				throw new IllegalArgumentException("Cannot invoke "+method+". Invalid arguments count. 0,1,2 expected");
			} 
		}catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
		
	}


	public static List<MethodListener> createListeners(Object ob){
		ArrayList<MethodListener> out = new ArrayList<MethodListener>();
		
		Class<?> scanningClazz = ob.getClass();
		while(Object.class != scanningClazz){
			Method[] methods = scanningClazz.getDeclaredMethods();
			for (Method candidat : methods) {
				Class<? extends Event> eventType = getEventType(candidat);
				if(eventType != null){
					MethodListener listener = new MethodListener(eventType, ob, candidat);
					out.add(listener);
				}
			}
			scanningClazz = scanningClazz.getSuperclass();
		}
		
		return out;
	}
	
	@SuppressWarnings("unchecked")
	private static Class<? extends Event> getEventType(Method method){
		if(method.getAnnotation(EventListener.class) == null 
				&& method.getAnnotation(LastEventListener.class) == null
				&& method.getAnnotation(FirstEventListener.class) == null){
			return null;
		}
				
		
		Class<?>[] parameterTypes = method.getParameterTypes();
		if(parameterTypes != null && parameterTypes.length > 0){
			return (Class<? extends Event>)parameterTypes[0];
		}
		
		throw new IllegalStateException("unknow type of "+method);
	}
	
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MethodListener [eventType=" + eventType + ", domainKeys=[");
		
		int lastDomainKeyIndex = domainKeys.size()-1;
		for(int i = 0; i < lastDomainKeyIndex; i++){
			WeakReference<Object> ref = domainKeys.get(i);
			sb.append(ref.get()).append(", ");
		}
		sb.append(domainKeys.get(lastDomainKeyIndex).get());
		
		sb.append("], listenerObjectRef=" + listenerObjectRef.get()+ ", method=" + method + "]");
		return sb.toString();
	}



}
