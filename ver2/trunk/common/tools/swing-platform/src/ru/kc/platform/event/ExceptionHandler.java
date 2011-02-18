package ru.kc.platform.event;

public interface ExceptionHandler {
	
	void handle(Throwable e) throws RuntimeException;

}
