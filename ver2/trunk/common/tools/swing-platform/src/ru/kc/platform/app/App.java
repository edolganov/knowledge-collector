package ru.kc.platform.app;

import java.awt.Container;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.command.CommandService;
import ru.kc.platform.controller.AbstractController;
import ru.kc.platform.controller.ControllerScan;
import ru.kc.platform.module.ModuleScan;
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
	ArrayList<String> rootControllersPackages = new ArrayList<String>();
	ArrayList<Object> dataForInject = new ArrayList<Object>();
	Container rootUI;
	
	//app data
	ScriptsService scriptsService;
	CommandService commandService;
	AppContext context;
	ArrayList<AbstractController<?>> rootControllers = new ArrayList<AbstractController<?>>();
	
	public void setRootUI(JFrame rootUI) {
		this.rootUI = rootUI;
	}
	
	public void addScriptsDevDir(File file) {
		scriptsDevDirs.add(file);
	}
	
	public void addScriptsProdactionDir(File file) {
		scriptsProdactionDirs.add(file);
	}
	
	public void addRootControllersPackage(String packageName) {
		rootControllersPackages.add(packageName);
	}
	
	public void addContextData(Object data){
		dataForInject.add(data);
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
		initUI();
		showUI();
		
	}

	private void initServices() {
		scriptsService = new ScriptsService(new ScriptServiceControlleImpl());
		addCodeDirs(scriptsService);
		
		commandService = new CommandService();
	}

	private void initContext() {
		context = new AppContext(
				rootUI,
				scriptsService,
				dataForInject,
				commandService);	
		AppContext.put(rootUI, context);
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

	private void initUI() {
        //init modules
        new ModuleScan(context).scanAndInit(context.rootUI);
        
        //root's controllers
        ControllerScan controllerScan = new ControllerScan(context);
        for(String packageName : rootControllersPackages){
			rootControllers.addAll(controllerScan.scanAndInit(packageName, context.rootUI));
        }
        log.info("inited root controllers: count="+rootControllers.size()+", "+rootControllers);

        
		//scripts controllers
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
