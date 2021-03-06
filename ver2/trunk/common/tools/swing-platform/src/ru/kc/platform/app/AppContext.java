package ru.kc.platform.app;

import java.awt.Container;
import java.util.HashMap;
import java.util.List;

import ru.kc.platform.command.CommandService;
import ru.kc.platform.event.EventManager;
import ru.kc.platform.global.GlobalObjects;
import ru.kc.platform.runtimestorage.RuntimeStorage;
import ru.kc.tools.scriptengine.ScriptsService;
import ru.kc.util.swing.config.ComponentScanner;

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
	public final EventManager eventManager;
	public final RuntimeStorage runtimeStorage;
	public final GlobalObjects globalObjects;
	public final ComponentScanner componentScanner;

	public AppContext(Container rootUI, ScriptsService scriptsService,
			List<Object> dataForInject, CommandService commandService,
			EventManager eventManager,
			RuntimeStorage runtimeStorage,
			GlobalObjects globalObjects, 
			ComponentScanner componentScanner) {
		super();
		this.rootUI = rootUI;
		this.scriptsService = scriptsService;
		this.dataForInject = dataForInject;
		this.commandService = commandService;
		this.eventManager = eventManager;
		this.runtimeStorage = runtimeStorage;
		this.globalObjects = globalObjects;
		this.componentScanner = componentScanner;
	}






	
	
	

}
