package ru.dolganov.tool.knowledge.collector;

import java.util.HashMap;
import java.util.Map;

public class AppUtil {
	
	public static Map<String, Object> map(){
		return new HashMap<String, Object>();
	}
	
	public static Map<String, Object> map(int count){
		return new HashMap<String, Object>(count);
	}
	
	public static Map<String, Object> map(String key1, Object value1){
		Map<String, Object> map = map(1);
		map.put(key1, value1);
		return map;
	}

}
