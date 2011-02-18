package ru.kc.tools.filepersist.update;

public class UpdateName implements UpdateRequest {
	
	public final String value;

	public UpdateName(String value) {
		if(value == null) throw new IllegalArgumentException(value+" is null");
		this.value = value;
	}

	@Override
	public Object value() {
		return value;
	}

}
