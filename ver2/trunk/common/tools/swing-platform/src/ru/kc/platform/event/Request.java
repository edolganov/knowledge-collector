package ru.kc.platform.event;

public class Request<T> extends Event {
	
	private T responseData;
	
	public void setResponse(T data){
		this.responseData = data;
	}
	
	public T getResponse(){
		return responseData;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+" [responseData=" + responseData + "]";
	}
	
	

	
	
	
	
	
}
