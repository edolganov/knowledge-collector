package ru.kc.platform.app;

import java.awt.Component;

import ru.kc.tools.scriptengine.ScriptsService;

public class AppContext {
	
	public final Component rootUI;
	public final ScriptsService scriptsService;
	
	public AppContext(Component rootUI, ScriptsService scriptsService) {
		super();
		this.rootUI = rootUI;
		this.scriptsService = scriptsService;
	}
	
	
	
	

}
