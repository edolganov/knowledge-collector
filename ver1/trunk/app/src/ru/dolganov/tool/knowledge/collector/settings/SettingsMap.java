package ru.dolganov.tool.knowledge.collector.settings;

import java.util.LinkedHashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("app-settings")
public class SettingsMap {
	
	Map<String, String> map;

	public Map<String, String> getMap() {
		if(map == null) map = new LinkedHashMap<String, String>();
		return map;
	}
	
	public Map<String, String> map() {
		return getMap();
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	
}
