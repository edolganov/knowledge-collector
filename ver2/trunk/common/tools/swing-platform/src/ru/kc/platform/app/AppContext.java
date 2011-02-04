package ru.kc.platform.app;

import java.awt.Container;

import ru.kc.tools.scriptengine.ScriptsService;

public class AppContext {
	
	public final Container rootUI;
	public final ScriptsService scriptsService;
	
	public AppContext(Container rootUI, ScriptsService scriptsService) {
		super();
		this.rootUI = rootUI;
		this.scriptsService = scriptsService;
	}
	
	
	
	

}
