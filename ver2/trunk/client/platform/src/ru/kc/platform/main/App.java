package ru.kc.platform.main;

import java.io.File;

import ru.kc.tools.scriptengine.ScriptsService;

public class App {

	public void init() {
		initScriptService();
		initContext();
		initUI();
		showUI();
		
	}

	private void initScriptService() {
		ScriptsService scriptsService = new ScriptsService();
		scriptsService.addCodeBase(new File("./client"));
		
	}

	private void initContext() {
		// TODO Auto-generated method stub
		
	}

	private void initUI() {
		// TODO Auto-generated method stub
		
	}

	private void showUI() {
		// TODO Auto-generated method stub
		
	}

}
