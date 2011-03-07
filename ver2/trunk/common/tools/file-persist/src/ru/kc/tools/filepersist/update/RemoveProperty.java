package ru.kc.tools.filepersist.update;

public class RemoveProperty implements UpdateRequest {
	
	public final String key;

	public RemoveProperty(String value) {
		this.key = value;
	}

	@Override
	public Object value() {
		return key;
	}

}
