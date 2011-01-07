package ru.chapaj.util.app;

public class GenericApp {
	
	private static GenericApp def;
	
	public static GenericApp getDefault(){
		if(def == null) def = new GenericApp();
		return def;
	}
}
