package ru.kc.platform.profile;

import java.util.LinkedHashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("profile")
public class ProfileImpl implements Profile {
	
	Map<String, String> map = new LinkedHashMap<String, String>();

	@Override
	public String get(String key) {
		return map.get(key);
	}

	@Override
	public void put(String key, String param) {
		map.put(key, param);
	}
	
	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

}
