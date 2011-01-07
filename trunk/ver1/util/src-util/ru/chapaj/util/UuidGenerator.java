package ru.chapaj.util;

import java.util.Random;

public class UuidGenerator {
	
	private static Random random = new Random();
	
	/**
	 * ""+System.currentTimeMillis()+""+[random 3 number]
	 * @return
	 */
	public static String simpleUuid(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(System.currentTimeMillis());
		for(int i=0;i<3;++i)
		sb.append(random.nextInt(10));
		
		return sb.toString();
	}

}
