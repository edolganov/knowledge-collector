package ru.kc.platform.scripts;

import ru.kc.platform.scripts.annotations.Mapping;
import ru.kc.tools.scriptengine.ScriptServiceController;

public class ScriptServiceControlleImpl implements ScriptServiceController {
	
	@Override
	public Object getMapping(Object ob) {
		Class<?> obClass = ob.getClass();
		Mapping annotation = obClass.getAnnotation(Mapping.class);
		if(annotation == null) return null;
		
		Class<?> uiClass = annotation.value();
		
		return ""+uiClass+"-"+obClass;
	}

}
