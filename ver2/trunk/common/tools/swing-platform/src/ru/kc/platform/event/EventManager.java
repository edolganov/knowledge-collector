package ru.kc.platform.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.data.Answer;
import ru.kc.platform.domain.DomainMember;
import ru.kc.util.swing.SwingUtil;



public class EventManager {
	
	private static final Log log = LogFactory.getLog(EventManager.class);
	private Listeners listeners = new Listeners();
	
	
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		listeners.setExceptionHandler(exceptionHandler);
	}
	

	public void fireEventInEDT(final DomainMember source, final Event event){
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				fireEvent(source, event);
			}
		};
		SwingUtil.invokeInEDT(runnable);
	}
	
	public <N> Answer<N> fireSaveRequestInEDT(DomainMember source, Request<N> request){
		try {
			N result = fireRequestInEDT(source, request);
			return new Answer<N>(result);
		}catch (Throwable t) {
			log.error("invoke error", t);
			if(t instanceof Exception){
				return new Answer<N>((Exception)t);
			} else {
				return new Answer<N>(new IllegalStateException(t));
			}
		}
	}
	
	public <T> T fireRequestInEDT(final DomainMember source, final Request<T> request) throws Throwable {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				fireEvent(source, request);
			}
		};
		SwingUtil.invokeInEDTAndWait(runnable);
		if(request.isProcessed())
			return request.getResponse();
		else 
			throw new IllegalStateException("no response for "+request);
	}

	
	private void fireEvent(DomainMember source, Event event){
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
