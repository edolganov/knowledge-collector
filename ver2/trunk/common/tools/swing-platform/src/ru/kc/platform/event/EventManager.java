package ru.kc.platform.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.util.swing.SwingUtil;



public class EventManager {
	
	private static final Log log = LogFactory.getLog(EventManager.class);
	private Listeners listeners = new Listeners();
	
	
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		listeners.setExceptionHandler(exceptionHandler);
	}
	

	public void fireEventInEDT(final Object source, final Event event){
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				fireEvent(source, event);
			}
		};
		SwingUtil.invokeInEDT(runnable);
	}
	
	public <T> T fireRequestInEDT(final Object source, final Request<T> request) throws Throwable {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				fireEvent(source, request);
			}
		};
		SwingUtil.invokeInEDTAndWait(runnable);
		return request.getResponse();
	}

	
	private void fireEvent(Object source, Event event){
		listeners.processEvent(source, event);
	}
	

	public void addObjectMethodListeners(Object ob){
		listeners.addObjectMethodListeners(ob);
	}
	
	public void removeObjectMethodListener(Object ob){
		int count = listeners.removeObjectMethodListener(ob);
		if(count > 0){
			log.info("unsubscribed " + ob);
		}
	}

}
