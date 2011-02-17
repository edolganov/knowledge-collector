package ru.kc.platform.event;

public class Event {
	
	private Object sender;

	public Event() {
		super();
	}

	public Object getSender() {
		return sender;
	}

	public void setSender(Object sender) {
		this.sender = sender;
	}

	@Override
	public String toString() {
		return "Event [sender=" + sender + "]";
	}
	
	

}
