package ru.kc.platform.scripts.controller;

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
			tryInit(ob, mapping, type);
		}
		
	}

	public void init(Object ob, String type) {
		Class<? extends Object> mapping = ob.getClass();
		List<String> types = scriptsService.getTypesByMapping(mapping);
		for (String candidat : types) {
			if(candidat.equals(type)){
				tryInit(ob, mapping, type);
				break;
			}
		}
	}
	
	private void tryInit(Object ob, Class<? extends Object> mapping, String type) {
		try{
			init(ob, mapping, type);
		}catch (Exception e) {
			log.error("init error",e);
		}
	}

	private void init(Object ob, Class<? extends Object> mapping, String type)
			throws Exception {
		InstanceDelegate inst = scriptsService.createInstance(mapping, type);
		inst.invoke("init", ob);
		inst.invoke("init");
	}

}
