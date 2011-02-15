package ru.kc.platform.runtimestorage;

import java.util.HashMap;
import java.util.WeakHashMap;

public class RuntimeStorage {

	private WeakHashMap<Object, HashMap<Object, Object>> storage = new WeakHashMap<Object, HashMap<Object,Object>>();
	
	
	public synchronized void putWithWeakReferenceDomain(Object domain, Object key, Object value) {
		HashMap<Object, Object> data = storage.get(domain);
		if(data == null){
			data = new HashMap<Object, Object>();
			storage.put(domain, data);
		}
		data.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T> T get(Object domain, Object key){
		HashMap<Object, Object> data = storage.get(domain);
		if(data == null) return null;
		return (T) data.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T> T remove(Object domain, Object key){
		HashMap<Object, Object> data = storage.get(domain);
		if(data == null) return null;
		
		T out = (T) data.remove(key);
		if(data.size() == 0){
			storage.remove(domain);
		}
		
		return out;
	}

}
