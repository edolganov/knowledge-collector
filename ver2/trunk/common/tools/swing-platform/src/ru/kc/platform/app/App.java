package ru.kc.platform.app;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.scripts.controller.ScriptControllerScan;
import ru.kc.platform.scripts.controller.ScriptServiceControlleImpl;
import ru.kc.tools.scriptengine.ScriptId;
import ru.kc.tools.scriptengine.ScriptServiceListener;
import ru.kc.tools.scriptengine.ScriptsService;

public class App {
	
	private static Log log = LogFactory.getLog(App.class);
	
	//init data
	ArrayList<File> scriptsDevDirs = new ArrayList<File>();
	ArrayList<File> scriptsProdactionDirs = new ArrayList<File>();
	Component rootUI;
	
	//app data
	ScriptsService scriptsService;
	AppContext context;
	
	public void setRootUI(JFrame rootUI) {
		this.rootUI = rootUI;
	}
	
	public void addScriptsDevDir(File file) {
		scriptsDevDirs.add(file);
	}
	
	public void addScriptsProdactionDir(File file) {
		scriptsProdactionDirs.add(file);
	}
	
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				init();
			}
		});
	}

	private void init() {
		initServices();
		initContext();
		initUIControllers();
		showUI();
		
	}

	private void initServices() {
		scriptsService = new ScriptsService(new ScriptServiceControlleImpl());
		addCodeDirs(scriptsService);
	}

	private void initContext() {
		context = new AppContext(rootUI,scriptsService);	
	}
	
	private void addCodeDirs(ScriptsService scriptsService) {
		boolean added = false;
		//dev?
		for (File file : scriptsDevDirs) {
			if(file.exists()){
				scriptsService.addCodeBase(file);
				added = true;
			}
		}
		if(added) return;
		
		//prodaction?
		for (File file : scriptsProdactionDirs) {
			if(file.exists()){
				scriptsService.addCodeBase(file);
				added = true;
			}
		}
		if(added) return;
		
		//unknow
		log.warn("can't find scripts files");
	}

	private void initUIControllers() {
		new ScriptControllerScan(context.scriptsService).scanAndInit(context.rootUI);
		context.scriptsService.addListener(new RootUIScriptListener(context));
	}

	private void showUI() {
		context.rootUI.setVisible(true);
	}







}



class RootUIScriptListener implements ScriptServiceListener {
	
	AppContext context;
	ScriptControllerScan scriptControllerScan;

	public RootUIScriptListener(AppContext context) {
		super();
		this.context = context;
		scriptControllerScan = new ScriptControllerScan(context.scriptsService);
	}
	
	@Override
	public void onScriptUpdated(ScriptId id) {
		onScriptCreated(id);
	}
	
	@Override
	public void onScriptCreated(ScriptId id) {
		if(id.domain == context.rootUI.getClass()){
			scriptControllerScan.init(context.rootUI,id);
		}
	}
	
	@Override
	public void onScriptDeleted(ScriptId id) {}
	
}
