package ru.kc.tools.filepersist.update;

public class UpdateDescription implements UpdateRequest {
	
	public final String value;

	public UpdateDescription(String value) {
		super();
		this.value = value;
	}

	@Override
	public Object value() {
		return value;
	}

}
