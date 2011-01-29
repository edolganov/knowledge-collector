package ru.kc.tools.scriptengine;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.scriptengine.model.Script;
import ru.kc.tools.scriptengine.model.annotations.Mapping;
import ru.kc.util.Check;

public class ScriptsService {
	
	private static Log log = LogFactory.getLog(ScriptsService.class);
	
	private GroovyClassLoader loader;
	private HashMap<String, Script> allScripts = new HashMap<String, Script>();
	
	
	public void init(File scriptsRootDir){
		
        loader = new GroovyClassLoader(this.getClass().getClassLoader());

		File[] files = scriptsRootDir.listFiles(new JavaFilesFilter());
		if(!Check.isEmpty(files)){
			for(File file : files){
				Script script = new Script(file,loader);
	            log.info("Registering: " + script);
	            
	            Object ob = script.createObject();
	            Mapping mapping = ob.getClass().getAnnotation(Mapping.class);
	            if(mapping == null){
	            	log.error("\t"+script+" doesn't have mapping");
	            	continue;
	            }
	            
	            if(mapping.value() == null){
	            	log.error("\t"+script+" have null mapping");
	            	continue;
	            }
	            
	            allScripts.put(mapping.value(), script);
			}
		}
		
		log.info("total script size: " + allScripts.size());
	}

}


class JavaFilesFilter implements FileFilter {
	
	@Override
	public boolean accept(File pathname) {
		if(pathname.isDirectory()) return false;
		String name = pathname.getName();
		name = name.toLowerCase();
		return name.endsWith(".java");
	}
}
