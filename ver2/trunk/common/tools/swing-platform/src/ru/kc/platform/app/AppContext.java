package ru.kc.platform.app;

import java.awt.Container;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ru.kc.platform.command.CommandService;
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
	public final List<Object> dataForInject;
	public final CommandService commandService;

	public AppContext(Container rootUI, ScriptsService scriptsService,
			List<Object> dataForInject, CommandService commandService) {
		super();
		this.rootUI = rootUI;
		this.scriptsService = scriptsService;
		this.dataForInject = dataForInject;
		this.commandService = commandService;
	}

	
	
	
	
	

}
