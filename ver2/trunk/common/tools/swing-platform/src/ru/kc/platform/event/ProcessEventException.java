package ru.kc.platform.event;

public class ProcessEventException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public ProcessEventException() {
		super();
	}

	public ProcessEventException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessEventException(String message) {
		super(message);
	}

	public ProcessEventException(Throwable cause) {
		super(cause);
	}

}
