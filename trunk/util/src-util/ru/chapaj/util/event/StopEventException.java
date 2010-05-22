package ru.chapaj.util.event;

public class StopEventException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public StopEventException() {
		super();
	}

	public StopEventException(String message, Throwable cause) {
		super(message, cause);
	}

	public StopEventException(String message) {
		super(message);
	}

	public StopEventException(Throwable cause) {
		super(cause);
	}

}
