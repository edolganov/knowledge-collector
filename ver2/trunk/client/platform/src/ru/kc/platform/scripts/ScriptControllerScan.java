package ru.kc.platform.scripts;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.scriptengine.InstanceDelegate;
import ru.kc.tools.scriptengine.ScriptsService;

public class ScriptControllerScan {
	
	private static Log log = LogFactory.getLog(ScriptControllerScan.class);
	
	ScriptsService scriptsService;
	
	public ScriptControllerScan(ScriptsService scriptsService) {
		this.scriptsService = scriptsService;
	}
	
	public void scanAndInit(Object ob){
		Class<? extends Object> mapping = ob.getClass();
		List<String> types = scriptsService.getTypesByMapping(mapping);
		for (String type : types) {
			try{
				InstanceDelegate inst = scriptsService.createInstance(mapping, type);
				inst.invoke("init", ob);
				inst.invoke("init");
				
			}catch (Exception e) {
				log.error("init error",e);
			}
		}
		
	}

}
