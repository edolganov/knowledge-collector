package ru.kc.platform.event;

public class Request<T> extends Event {
	
	private boolean hasResponse = false;
	private T responseData;
	
	public void setResponse(T data){
		hasResponse = true;
		this.responseData = data;
	}
	
	public void setNullResponse(){
		setResponse(null);
	}
	
	public T getResponse(){
		return responseData;
	}

	public boolean hasResponse() {
		return hasResponse;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+" [hasResponse=" + hasResponse + ", response="
				+ responseData + "]";
	}
	
	
	
	
}
