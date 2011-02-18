package ru.kc.platform.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.domain.DomainUtil;
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
		Object domianKey = getDomianKey(source, event);
		
		Class<?> exitClass = Event.class.getSuperclass();
		Class<?> curClass = event.getClass();
		while(curClass != exitClass){
			ListenersQueue queue = listeners.get(curClass);
			if(queue != null){
				queue.removeInvalidListeners();
				for(MethodListener listener : queue){
					if(listener.belongsTo(domianKey)){
						try {
							listener.process(event);
						}catch (Throwable e) {
							handleListenerException(e);
						}
					}
				}
			}
			
			curClass = curClass.getSuperclass();
		}
		
	}

	private Object getDomianKey(Object source, Event event) {
		Object domainKey = DomainMember.ROOT_DOMAIN_KEY;
		if(DomainUtil.isDomainSpecific(event)){
			if(source instanceof DomainMember){
				domainKey = ((DomainMember) source).getDomainKey();
			} else {
				throw new IllegalStateException(source+" should be a DomainMember for firing domain specific "+event);
			}
			
		}
		return domainKey;
	}

	private void handleListenerException(Throwable e) {
		if(exceptionHandler != null) exceptionHandler.handle(e);
		else throw new ProcessEventException(e);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Listeners [ \n");
		for(Class<?> type : listeners.keySet()){
			ListenersQueue queue = listeners.get(type);
			sb.append("\t type=").append(type).append("\n");
			sb.append("\t listeners=").append(type).append("\n");
			for(MethodListener listener : queue){
				sb.append("\t\t").append(listener).append("\n");
			}
		}
		sb.append(", exceptionHandler=").append(exceptionHandler).append("]");
		return sb.toString();
	}
	
	


}
