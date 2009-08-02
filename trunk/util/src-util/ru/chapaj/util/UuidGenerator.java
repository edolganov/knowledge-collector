package ru.chapaj.util;

public class UuidGenerator {
	
	public static String simpleUuid(){
		String uuid;
		try {
			Thread.sleep(50);
			uuid = ""+System.currentTimeMillis();
		}
		catch (Exception e) {
			uuid = ""+System.nanoTime();
		}
		return uuid;
	}

}
