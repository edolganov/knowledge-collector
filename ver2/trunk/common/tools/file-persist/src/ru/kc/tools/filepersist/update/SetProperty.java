package ru.kc.tools.filepersist.update;

import ru.kc.util.collection.Pair;

public class SetProperty implements UpdateRequest {
	
	public final Pair<String, String> keyValue;
	public final Object additionInfo;

	public SetProperty(String key, String value) {
		this(key, value, null);
	}
	
	public SetProperty(String key, String value, Object additionInfo) {
		keyValue = new Pair<String, String>(key, value);
		this.additionInfo = additionInfo;
	}

	@Override
	public Pair<String, String> value() {
		return keyValue;
	}

}
