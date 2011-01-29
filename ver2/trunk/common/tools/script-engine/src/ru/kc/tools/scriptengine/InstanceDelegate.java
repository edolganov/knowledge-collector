package ru.kc.tools.scriptengine;

import ru.kc.tools.scriptengine.exception.InvocationException;
import ru.kc.tools.scriptengine.exception.ScriptNotExistException;
import ru.kc.tools.scriptengine.model.Script;

public class InstanceDelegate {
	
	private Object curInstance;
	private String type;
	private Object mapping;
	private ScriptsService service;

	public InstanceDelegate(Object curInstance, Object mapping, String type, ScriptsService service) {
		super();
		this.curInstance = curInstance;
		this.service = service;
		this.type = type;
		this.mapping = mapping;
	}
	
	public <T> T invoke(String method) throws Exception{
		return invoke(method, (Object[])null);
	}
	
	public <T> T invoke(String method, Object... args) throws Exception{
		if(curInstance == null) throw new ScriptNotExistException();
	
		Script script = service.getScript(mapping,type);
		if(script == null) throw new ScriptNotExistException();
		
		try {
			return script.invoke(curInstance, method, args);
		}catch (Exception e) {
			throw new InvocationException(e);
		}
	}
	

}
