package ru.kc.platform.service;

public interface ServiceContainer {
	
	void addService(Class<?> type, Object ob);
	
	boolean containsService(Class<?> type);
	
	Object getService(Class<?> type);

}
