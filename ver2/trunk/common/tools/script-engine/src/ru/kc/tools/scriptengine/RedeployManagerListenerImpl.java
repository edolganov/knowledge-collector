package ru.kc.tools.scriptengine;

import java.io.File;

import ru.kc.util.file.redeploy.RedeployManagerListener;

public class RedeployManagerListenerImpl implements RedeployManagerListener{
	
	ScriptsService scriptsService;
	
	public RedeployManagerListenerImpl(ScriptsService scriptsService) {
		super();
		this.scriptsService = scriptsService;
	}

	@Override
	public void onCreate(String path) {
		File file = new File(path);
		if(file.isDirectory()){
			scriptsService.initRecursive(file);
		}else {
			scriptsService.registerScriptRequest(file);
		}
	}
	
	@Override
	public void onUpdate(String path) {
		scriptsService.registerScriptRequest(new File(path));
	}
	
	@Override
	public void onDelete(String path) {
		scriptsService.removeByPath(path);
	}

}
