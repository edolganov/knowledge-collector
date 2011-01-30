package ru.kc.tools.scriptengine;

import ru.kc.tools.scriptengine.exception.InvocationException;
import ru.kc.tools.scriptengine.exception.ScriptNotExistException;
import ru.kc.tools.scriptengine.model.Script;

public class InstanceDelegate {
	
	private Object curInstance;
	private ScriptsService service;
	private Script curScript;

	public InstanceDelegate(Object curInstance, Script script, ScriptsService service) {
		super();
		this.curInstance = curInstance;
		this.service = service;
		this.curScript = script;
	}
	
	public <T> T invoke(String method) throws Exception{
		return invoke(method, (Object[])null);
	}
	
	public <T> T invoke(String method, Object... args) throws Exception{	
		Script lastScript = service.getScript(curScript.getId());
		if(lastScript == null) throw new ScriptNotExistException();
		
		try {
			if(curScript != lastScript){
				//reload data
				curInstance = curScript.createInstance();
				curScript = lastScript;
			}
			
			return curScript.invoke(curInstance, method, args);
		}catch (Exception e) {
			throw new InvocationException(e);
		}
	}
	

}
