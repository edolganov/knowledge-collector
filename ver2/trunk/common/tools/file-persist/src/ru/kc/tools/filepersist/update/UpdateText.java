package ru.kc.tools.filepersist.update;

public class UpdateText implements UpdateRequest {
	
	public final String value;

	public UpdateText(String value) {
		this.value = value;
	}

	@Override
	public Object value() {
		return value;
	}

}
