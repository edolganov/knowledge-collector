package ru.kc.tools.scriptengine;

public interface ScriptServiceListener {
	
	void onScriptCreated(Object mapping, String type);
	
	void onScriptUpdated(Object mapping, String type);
	


}
