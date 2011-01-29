package ru.kc.platform.main;

import ru.kc.platform.main.ui.MainForm;
import ru.kc.tools.scriptengine.ScriptsService;

public class AppContext {
	
	MainForm rootUI;
	ScriptsService scriptsService;
	
	

	public ScriptsService getScriptsService() {
		return scriptsService;
	}

	public MainForm getRootUI() {
		return rootUI;
	}
	
	

}
