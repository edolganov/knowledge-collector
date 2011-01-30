package ru.kc.platform.scripts.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.scripts.annotations.Mapping;
import ru.kc.tools.scriptengine.ScriptId;
import ru.kc.tools.scriptengine.ScriptServiceController;

public class ScriptServiceControlleImpl implements ScriptServiceController {
	
	private static Log log = LogFactory.getLog(ScriptServiceControlleImpl.class);
	
	@Override
	public ScriptId getId(Class<?> type) {
		Mapping annotation = type.getAnnotation(Mapping.class);
		if(annotation == null){
			log.info("no annotation "+Mapping.class);
			return null;
		}

		
		if(!ScriptController.class.isAssignableFrom(type)) {
			log.info("no superclass "+ScriptController.class);
			return null;
		}
		
		Class<?> domain = annotation.value();
		String name = type.getSimpleName();
		return new ScriptId(domain, name);
	}

}
