package ru.kc.tools.filepersist.update;

public class UpdatePath implements UpdateRequest {
	
	public final String value;

	public UpdatePath(String value) {
		this.value = value;
	}

	@Override
	public Object value() {
		return value;
	}

}
