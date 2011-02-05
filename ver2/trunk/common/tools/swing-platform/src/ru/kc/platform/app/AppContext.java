package ru.kc.platform.app;

import java.awt.Container;
import java.util.HashMap;

import ru.kc.tools.scriptengine.ScriptsService;

public final class AppContext {
	
	private static HashMap<Object, AppContext> contexts = new HashMap<Object, AppContext>();
	
	public static void put(Object key, AppContext context){
		contexts.put(key, context);
	}
	
	public static AppContext get(Object key){
		return contexts.get(key);
	}
	
	public static int contextsCount(){
		return contexts.size();
	}
	
	public final Container rootUI;
	public final ScriptsService scriptsService;
	
	public AppContext(Container rootUI, ScriptsService scriptsService) {
		super();
		this.rootUI = rootUI;
		this.scriptsService = scriptsService;
	}
	
	
	
	

}
