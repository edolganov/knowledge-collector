package ru.chapaj.util;

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
}
