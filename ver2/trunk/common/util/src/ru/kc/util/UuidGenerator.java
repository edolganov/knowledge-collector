package ru.kc.util;

public class UuidGenerator {
	
	public static String simpleUuid(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(System.currentTimeMillis());
		sb.append('_');
		sb.append(System.nanoTime());
		
		return sb.toString();
	}

}
