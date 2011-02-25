package ru.kc.platform.service;

import java.util.HashMap;

public class ServiceContainerImpl implements ServiceContainer {
	
	private HashMap<Class<?>, Object> map = new HashMap<Class<?>, Object>();

	@Override
	public void addService(Class<?> type, Object ob) {
		map.put(type, ob);
	}

	@Override
	public boolean containsService(Class<?> type) {
		return map.containsKey(type);
	}

	@Override
	public Object getService(Class<?> type) {
		return map.get(type);
	}

}
