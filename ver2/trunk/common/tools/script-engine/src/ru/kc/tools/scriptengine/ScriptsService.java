package ru.kc.tools.scriptengine;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.scriptengine.model.Script;
import ru.kc.tools.scriptengine.model.annotations.Mapping;
import ru.kc.util.Check;

public class ScriptsService {
	
	private static Log log = LogFactory.getLog(ScriptsService.class);
	
	private GroovyClassLoader loader;
	private HashMap<String, Script> allScripts = new HashMap<String, Script>();
	
	public ScriptsService() {
        loader = new GroovyClassLoader(this.getClass().getClassLoader());
	}
	
	
	public void addCodeBase(File scriptsRootDir) {
        initRecursive(scriptsRootDir);
	}


	private void initRecursive(File scriptsRootDir) {
		
		LinkedList<File> queue = new LinkedList<File>();
		queue.addLast(scriptsRootDir);
		while(!queue.isEmpty()){
			File curDir = queue.removeFirst();
			initScripts(curDir);
			addSubDirs(queue, curDir);
		}
		
		log.info("inited scripts count: " + allScripts.size());
	}


	private void initScripts(File curDir) {
		File[] files = curDir.listFiles(new JavaFilesFilter());
		if(!Check.isEmpty(files)){
			for(File file : files){
				registerScriptRequest(file);
			}
		}
	}

	
	private void addSubDirs(LinkedList<File> queue, File curDir) {
		File[] dirs = curDir.listFiles(new DirFilter());
		if(!Check.isEmpty(dirs)){
			for (File dir : dirs) {
				queue.addLast(dir);
			}
		}
	}
	
	private void registerScriptRequest(File file) {
		Script script = new Script(file,loader);
		log.info("Registering: " + script);
		
		Object ob = null;
		try {
			ob = script.createObject();
		}catch (Exception e) {
			log.error("parse expcetion",e);
			return;
		}
		
		Mapping mapping = ob.getClass().getAnnotation(Mapping.class);
		if(mapping == null){
			log.info("\tSkip because "+ob+" doesn't have @Mapping annotation");
			return;
		}
		
		if(mapping.value() == null){
			log.error("\tSkip because "+ob+" have null mapping");
			return;
		}
		
		allScripts.put(mapping.value(), script);
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

class DirFilter implements FileFilter {
	
	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory();
	}
}
