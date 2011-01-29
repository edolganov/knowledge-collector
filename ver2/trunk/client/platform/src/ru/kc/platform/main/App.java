package ru.kc.platform.main;

import java.io.File;

import ru.kc.platform.main.ui.MainForm;
import ru.kc.platform.scripts.ScriptControllerScan;
import ru.kc.platform.scripts.ScriptServiceControlleImpl;
import ru.kc.tools.scriptengine.ScriptsService;

public class App {
	
	AppContext context;

	public void init() {
		initContext();
		initUI();
		showUI();
		
	}

	private void initContext() {
		context = new AppContext();
		
		ScriptsService scriptsService = new ScriptsService(new ScriptServiceControlleImpl());
		scriptsService.addCodeBase(new File("./client/platform/script-src"));
		context.scriptsService = scriptsService;
		
	}

	private void initUI() {
		context.rootUI = new MainForm();
		
		new ScriptControllerScan(context.scriptsService).scanAndInit(context.rootUI);
	}

	private void showUI() {
		context.rootUI.setVisible(true);
		
	}

}
