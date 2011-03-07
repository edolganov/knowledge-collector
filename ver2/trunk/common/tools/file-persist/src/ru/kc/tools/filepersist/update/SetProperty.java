package ru.kc.tools.filepersist.update;

import ru.kc.util.collection.Pair;

public class SetProperty implements UpdateRequest {
	
	public final Pair<String, String> keyValue;

	public SetProperty(String key, String value) {
		keyValue = new Pair<String, String>(key, value);
	}

	@Override
	public Object value() {
		return keyValue;
	}

}
