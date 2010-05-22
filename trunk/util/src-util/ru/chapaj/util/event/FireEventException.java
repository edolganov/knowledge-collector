package ru.chapaj.util.event;

public class FireEventException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public FireEventException() {
		super();
	}

	public FireEventException(String message, Throwable cause) {
		super(message, cause);
	}

	public FireEventException(String message) {
		super(message);
	}

	public FireEventException(Throwable cause) {
		super(cause);
	}

}
