package ru.kc.platform.event;

public class Event {
	
	private Object sender;
	private boolean processed = false;

	public Event() {
		super();
	}

	public Object getSender() {
		return sender;
	}

	void setSender(Object sender) {
		this.sender = sender;
	}
	
	void setProcessed(){
		processed = true;
	}

	boolean isProcessed() {
		return processed;
	}

	@Override
	public String toString() {
		return "Event [sender=" + sender + "]";
	}

	
	
	

}
