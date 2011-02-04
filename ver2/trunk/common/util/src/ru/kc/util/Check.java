package ru.kc.util;

import java.util.Collection;

public class Check {
	
	public static boolean isEmpty(String s){
		if(s == null || s.length() == 0) return true;
		return false;
	}
	
	public static boolean isEmpty(Collection<?> collection){
		if(collection == null || collection.size() == 0) return true;
		return false;
	}

	public static boolean isEmpty(Object[] array) {
		return array == null|| array.length == 0;
	}
	
	
	public static boolean contains(Object ob, Object[] array) {
		if(array.length == 0) return false;
		
		for (Object object : array) {
			if(object.equals(ob)){
				return true;
			}
		}
		return false;
	}

}
