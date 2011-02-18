package ru.kc.platform.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.kc.platform.event.model.ListenersQueue;
import ru.kc.platform.event.model.MethodListener;

public class Listeners {
	
	private ExceptionHandler exceptionHandler;
	private Map<Class<?>, ListenersQueue> listeners = new HashMap<Class<?>, ListenersQueue>();
	
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public void addObjectMethodListeners(Object ob) {
		List<MethodListener> list = MethodListener.createListeners(ob);
		for(MethodListener l : list)
			add(l);
		
	}

	private void add(MethodListener listener) {
		Class<?> type = listener.getEventType();
		ListenersQueue queue = listeners.get(type);
		if(queue == null){
			queue = new ListenersQueue();
			listeners.put(type, queue);
		}
		queue.add(listener);
	}

	public int removeObjectMethodListener(Object ob) {
		int count = 0;
		List<MethodListener> list = MethodListener.createListeners(ob);
		for(MethodListener l : list){
			count = count + removeLike(l);
		}
		return count;
	}

	private int removeLike(MethodListener listener) {
		Class<?> type = listener.getEventType();
		ListenersQueue queue = listeners.get(type);
		if(queue != null){
			return queue.removeLike(listener);
		} else {
			return 0;
		}
	}

	public void processEvent(Object source, Event event) {
		event.setSender(source);
		
		Class<?> exitClass = Event.class.getSuperclass();
		Class<?> curClass = event.getClass();
		while(curClass != exitClass){
			ListenersQueue queue = listeners.get(curClass);
			if(queue != null){
				queue.removeInvalidListeners();
				for(MethodListener listener : queue){
					try {
						listener.process(event);
					}catch (Throwable e) {
						handleListenerException(e);
					}
				}
			}
			
			curClass = curClass.getSuperclass();
		}
		
	}

	private void handleListenerException(Throwable e) {
		if(exceptionHandler != null) exceptionHandler.handle(e);
		else throw new ProcessEventException(e);
	}

	@Override
	public String toString() {
		return "Listeners [listeners=" + listeners + ", exceptionHandler="
				+ exceptionHandler + "]";
	}
	
	


}
