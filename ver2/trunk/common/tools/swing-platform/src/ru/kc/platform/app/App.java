package ru.kc.platform.app;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.command.CommandService;
import ru.kc.platform.common.event.AppClosing;
import ru.kc.platform.common.event.AppInited;
import ru.kc.platform.common.event.CloseRequest;
import ru.kc.platform.controller.ControllerScan;
import ru.kc.platform.controller.ControllersPool;
import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.event.EventManager;
import ru.kc.platform.event.ExceptionHandler;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.platform.global.GlobalObjects;
import ru.kc.platform.init.InitializerScan;
import ru.kc.platform.module.ModuleScan;
import ru.kc.platform.runtimestorage.RuntimeStorage;
import ru.kc.platform.scripts.controller.ScriptControllerScan;
import ru.kc.platform.scripts.controller.ScriptServiceControlleImpl;
import ru.kc.tools.scriptengine.ScriptId;
import ru.kc.tools.scriptengine.ScriptServiceListener;
import ru.kc.tools.scriptengine.ScriptsService;
import ru.kc.util.swing.config.ComponentScanner;
import ru.kc.util.swing.config.ObjectHandler;

public class App implements DomainMember{
	
	private static Log log = LogFactory.getLog(App.class);
	
	//init data
	ArrayList<File> scriptsDevDirs = new ArrayList<File>();
	ArrayList<File> scriptsProdactionDirs = new ArrayList<File>();
	ArrayList<String> rootControllersPackages = new ArrayList<String>();
	ArrayList<Object> dataForInject = new ArrayList<Object>();
	ArrayList<String> globalObjectPackages = new ArrayList<String>();
	ArrayList<ObjectHandler> uiObjectHandlers = new ArrayList<ObjectHandler>();
	
	Container rootUI;
	ControllersPool rootControllers = new ControllersPool();
	
	
	//app data
	AppContext context;
	ScriptsService scriptsService;
	CommandService commandService;
	EventManager eventManager;
	RuntimeStorage runtimeStorageService;
	GlobalObjects globalObjects;
	ComponentScanner componentScanner;

	
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
	
	public void addGlobalObjectsPackagePreffix(String preffix){
		globalObjectPackages.add(preffix);
	}
	
	public void addUIObjectHandler(ObjectHandler handler){
		uiObjectHandlers.add(handler);
	}
	
	public void init() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				initFirstInitializers();
				initServices();
				initContext();
			}
		});
	}

	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				initUI();
				showUI();
			}
		});
	}


	protected void initFirstInitializers() {
		InitializerScan initializerScan = new InitializerScan();
		for(String preffix : globalObjectPackages){
			initializerScan.scanAndInvokeFirstInitializers(preffix);
		}
	}
	

	private void initServices() {
		scriptsService = new ScriptsService(new ScriptServiceControlleImpl());
		addCodeDirs(scriptsService);
		
		commandService = new CommandService();
		eventManager = new EventManager();
		eventManager.setExceptionHandler(new ExceptionHandler() {
			
			@Override
			public void handle(Throwable e) {
				log.error("error while fire event",e);
			}
		});
		eventManager.addObjectMethodListeners(this);
		
		runtimeStorageService = new RuntimeStorage();
		
		globalObjects = new GlobalObjects();
		for(String preffix : globalObjectPackages){
			globalObjects.scan(preffix);
		}

		componentScanner = new ComponentScanner();
		for (ObjectHandler handler : uiObjectHandlers) {
			componentScanner.addHandler(handler);
		}
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
	

	private void initContext() {
		context = new AppContext(
				rootUI,
				scriptsService,
				dataForInject,
				commandService,
				eventManager,
				runtimeStorageService,
				globalObjects,
				componentScanner);	
		AppContext.put(rootUI, context);
		
		commandService.setContext(context);
	}
	


	private void initUI() {
		initClosingEvent();
		
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
		
		eventManager.fireEventInEDT(this, new AppInited());
	}
	
	@EventListener
	public void onCloseRequest(CloseRequest request){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				closeRequest();
			}
		});
	}

	private void initClosingEvent() {
		if(rootUI instanceof Window){
			((Window)rootUI).addWindowListener(new WindowAdapter() {
				
				public void windowClosing(WindowEvent e) {
					closeRequest();					
				}
				
			});
		}
	}

	protected void closeRequest() {
		eventManager.fireEventInEDT(this, new AppClosing());
		System.exit(0);
	}

	private void showUI() {
		context.rootUI.setVisible(true);
	}

	public AppContext getInitedContext() {
		return context;
	}

	@Override
	public Object getDomainKey() {
		return DomainMember.ROOT_DOMAIN_KEY;
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
