package ru.kc.platform.profile;

public interface Profile {
	
	String get(String key);
	
	void put(String key, String param);

}
