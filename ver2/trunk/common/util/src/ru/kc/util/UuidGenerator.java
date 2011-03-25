package ru.kc.util;

import java.util.Random;

public class UuidGenerator {
	
	private static Random random = new Random();
	
	public static String simpleUuid(){
		return simpleUuid(3);
	}
	
	public static String simpleUuid(int randomCount){
		StringBuilder sb = new StringBuilder();
		
		sb.append(System.currentTimeMillis());
		for(int i=0;i<randomCount;++i)
		sb.append(random.nextInt(10));
		
		return sb.toString();
	}

}
