package ru.kc.tools.scriptengine;

public interface ScriptServiceListener {
	
	void onScriptCreated(ScriptId id);
	
	void onScriptUpdated(ScriptId id);
	
	void onScriptDeleted(ScriptId id);
	


}
