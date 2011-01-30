package ru.kc.platform.scripts.controller;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.scriptengine.InstanceDelegate;
import ru.kc.tools.scriptengine.ScriptId;
import ru.kc.tools.scriptengine.ScriptsService;

public class ScriptControllerScan {
	
	private static Log log = LogFactory.getLog(ScriptControllerScan.class);
	
	ScriptsService scriptsService;
	
	public ScriptControllerScan(ScriptsService scriptsService) {
		this.scriptsService = scriptsService;
	}
	
	public void scanAndInit(Object ob){
		Class<? extends Object> domain = ob.getClass();
		Set<Object> names = scriptsService.getNamesByDomain(domain);
		for (Object name : names) {
			tryInit(ob, domain, name);
		}
		
	}

	public void init(Object ob,ScriptId id) {
		tryInit(ob, id.domain, id.uniqueName);
	}
	
	private void tryInit(Object ob, Object domain, Object name) {
		try{
			init(ob, domain, name);
		}catch (Exception e) {
			log.error("init error",e);
		}
	}

	private void init(Object ob, Object domain, Object name)
			throws Exception {
		InstanceDelegate inst = scriptsService.createInstance(domain, name);
		inst.invoke("init", ob);
		inst.invoke("init");
	}

}
