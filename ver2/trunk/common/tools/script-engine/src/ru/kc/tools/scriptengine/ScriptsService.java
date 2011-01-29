package ru.kc.tools.scriptengine;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.scriptengine.model.Script;
import ru.kc.util.Check;
import ru.kc.util.file.redeploy.RedeployManager;

public class ScriptsService {
	
	public static final String FILE_EXT = ".java";
	private static Log log = LogFactory.getLog(ScriptsService.class);
	
	private GroovyClassLoader loader;
	private RedeployManager redeployManager = new RedeployManager(FILE_EXT, new RedeployManagerListenerImpl(this));
	
	//model
	private ReadWriteLock rw = new ReentrantReadWriteLock();
	private Lock readLock = rw.readLock();
	private Lock writeLock = rw.writeLock();
	private HashMap<String, Script> scriptsByMapping = new HashMap<String, Script>();
	private HashMap<String, Script> scriptsByAbsolutePath = new HashMap<String, Script>();
	
	public ScriptsService() {
        loader = new GroovyClassLoader(this.getClass().getClassLoader());
	}
	
	
	public void addCodeBase(File dir) {
        initRecursive(dir);
        redeployManager.addFilesDir(dir.getPath());
	}
	


	void initRecursive(File dir) {
		
		LinkedList<File> queue = new LinkedList<File>();
		queue.addLast(dir);
		while(!queue.isEmpty()){
			File curDir = queue.removeFirst();
			initScripts(curDir);
			addSubDirs(queue, curDir);
		}
		
		log.info("inited scripts count: " + getScriptsCount());
	}


	private void initScripts(File curDir) {
		File[] files = curDir.listFiles(new FilesFilter(FILE_EXT));
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
	
	void registerScriptRequest(File file) {
		Script script = new Script(file,loader);
		log.info("Registering: " + script);
		
		try {
			script.createScript();
		}catch (Exception e) {
			log.error(e.getMessage());
			return;
		}
		
		put(script);
	}


	
	private void put(Script script){
		String absolutePath = script.getFile().getAbsolutePath();
		
		writeLock.lock();
		try{
			Script oldByPath = scriptsByAbsolutePath.put(absolutePath, script);
			if(oldByPath != null)
				scriptsByMapping.remove(oldByPath.getMapping());
			
			scriptsByMapping.put(script.getMapping(), script);

		}finally{
			writeLock.unlock();
		}
	}
	
	void removeByPath(String path){
		String absolutePath = new File(path).getAbsolutePath();
		
		writeLock.lock();
		try{
			Script oldByPath = scriptsByAbsolutePath.remove(absolutePath);
			if(oldByPath != null)
				scriptsByMapping.remove(oldByPath.getMapping());

		}finally{
			writeLock.unlock();
		}
	}
	

	public <T> T invoke(String mapping, String method)throws Exception{
		return invoke(mapping, method, (Object[])null);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T invoke(String mapping, String method, Object... args)throws Exception{
		Script script = getByMapping(mapping);
		if(script == null) 
			throw new IllegalArgumentException("no script by mapping "+mapping);
		return (T)script.invoke(method, args);
	}
	
	private Script getByMapping(String mapping){
		readLock.lock();
		try{
			return scriptsByMapping.get(mapping);
		}finally{
			readLock.unlock();
		}
	}
	
	
	public int getScriptsCount(){
		readLock.lock();
		try{
			return scriptsByMapping.size();
		}finally{
			readLock.unlock();
		}
	}

}


class FilesFilter implements FileFilter {
	
	String ext;
	
	public FilesFilter(String ext){
		this.ext = ext;
	}
	
	@Override
	public boolean accept(File pathname) {
		if(pathname.isDirectory()) return false;
		String name = pathname.getName();
		name = name.toLowerCase();
		return name.endsWith(ext);
	}
}

class DirFilter implements FileFilter {
	
	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory();
	}
}
