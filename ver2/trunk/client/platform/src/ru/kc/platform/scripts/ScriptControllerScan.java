package ru.kc.platform.scripts;

import java.util.List;

import ru.kc.tools.scriptengine.ScriptsService;

public class ScriptControllerScan {
	
	ScriptsService scriptsService;
	
	public ScriptControllerScan(ScriptsService scriptsService) {
		this.scriptsService = scriptsService;
	}
	
	public void scanAndInit(Object ob){
		List<String> types = scriptsService.getTypes(ob);
		
		
	}

}
