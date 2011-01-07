package ru.chapaj.util.event;

public class Event<T> {
	
	private T data;
	
	public Event(){
		super();
	}

	public Event(T data) {
		super();
		this.data = data;
	}

	public T getData() {
		return data;
	}

}
