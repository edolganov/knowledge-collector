package ru.kc.platform.main;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.main.ui.MainForm;
import ru.kc.platform.scripts.controller.ScriptControllerScan;
import ru.kc.platform.scripts.controller.ScriptServiceControlleImpl;
import ru.kc.tools.scriptengine.ScriptId;
import ru.kc.tools.scriptengine.ScriptServiceListener;
import ru.kc.tools.scriptengine.ScriptsService;

public class App {
	
	private static Log log = LogFactory.getLog(App.class);
	
	AppContext context;

	public void init() {
		initContext();
		initUI();
		showUI();
		
	}

	private void initContext() {
		context = new AppContext();
		
		ScriptsService scriptsService = new ScriptsService(new ScriptServiceControlleImpl());
		File codeBase = getCodeBase();
		if(codeBase == null) log.warn("can't find scripts files");
		else scriptsService.addCodeBase(new File("./client/platform/script-src"));
		
		context.scriptsService = scriptsService;		
	}
	
	private File getCodeBase(){
		//dev
		File devCodeBase = new File("./client/platform/script-src");
		if(devCodeBase.exists()) return devCodeBase;
		
		devCodeBase = new File("./script-src");
		if(devCodeBase.exists()) return devCodeBase;
		
		//prodaction
		File prodactionCodeBase = new File("./data/scripts");
		if(prodactionCodeBase.exists()) return prodactionCodeBase;
		
		//empty
		return null;
	}

	private void initUI() {
		context.rootUI = new MainForm();
		
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
