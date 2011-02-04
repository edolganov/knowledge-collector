package ru.kc.platform.utils;

public class AppUtils {
	
	public static String toStringLikeObject(Object ob){
		return 	ob.getClass().getName() + "@" + Integer.toHexString(ob.hashCode());
	}


}
