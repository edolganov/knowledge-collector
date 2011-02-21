package ru.kc.tools.filepersist.update;

public class UpdateUrl implements UpdateRequest {
	
	public final String value;

	public UpdateUrl(String value) {
		this.value = value;
	}

	@Override
	public Object value() {
		return value;
	}

}
