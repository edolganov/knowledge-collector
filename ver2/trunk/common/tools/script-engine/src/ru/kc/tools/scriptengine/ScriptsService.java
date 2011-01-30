package ru.kc.tools.scriptengine;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.scriptengine.exception.ScriptNotExistException;
import ru.kc.tools.scriptengine.model.Script;
import ru.kc.util.Check;
import ru.kc.util.file.redeploy.RedeployManager;
import ru.kc.util.file.redeploy.RedeployManagerListener;

public class ScriptsService {
	
	public static final String FILE_EXT = ".java";
	private static Log log = LogFactory.getLog(ScriptsService.class);
	
	private ScriptServiceController serviceController;
	private GroovyClassLoader loader;
	private RedeployManager redeployManager = new RedeployManager(FILE_EXT, new RedeployManagerListenerImpl(this));
	private CopyOnWriteArrayList<ScriptServiceListener> listeners = new CopyOnWriteArrayList<ScriptServiceListener>();
	
	//model
	private ReadWriteLock rw = new ReentrantReadWriteLock();
	private Lock readLock = rw.readLock();
	private Lock writeLock = rw.writeLock();
	private HashMap<Object, HashMap<Object,Script>> scriptsById = new HashMap<Object, HashMap<Object,Script>>();
	private HashMap<String, Script> scriptsByAbsolutePath = new HashMap<String, Script>();
	
	public ScriptsService(ScriptServiceController serviceController) {
		this.serviceController = serviceController;
        loader = new GroovyClassLoader(this.getClass().getClassLoader());
        redeployManager.start();
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
		Script script = new Script(file,loader,serviceController);
		log.info("load script from file: " + file);
		
		try {
			script.init();
			put(script);
			log.info("registered: " + script);
		}catch (Exception e) {
			log.error("error registring script",e);
			return;
		}
		

	}


	
	private void put(Script script){
		String absolutePath = script.getAbsoluteFilePath();
		ScriptId id = script.getId();
		Script oldByPath = null;
		
		writeLock.lock();
		try{
			checkIdUnique(script);
			
			oldByPath = scriptsByAbsolutePath.put(absolutePath, script);
			removeFromIdMapIfCan(oldByPath);
			
			HashMap<Object, Script> map = scriptsById.get(id.domain);
			if(map == null){
				map = new HashMap<Object, Script>();
				scriptsById.put(id.domain, map);
			}
				
			map.put(id.uniqueName, script);
		}finally{
			writeLock.unlock();
		}
		
		if(oldByPath != null){
			for (ScriptServiceListener l : listeners) l.onScriptUpdated(id);
		} else {
			for (ScriptServiceListener l : listeners) l.onScriptCreated(id);
		}
		
	}
	
	private void checkIdUnique(Script script) {
		ScriptId id = script.getId();
		String absoluteFilePath = script.getAbsoluteFilePath();
		
		HashMap<Object, Script> map = scriptsById.get(id.domain);
		if(map != null){
			Script oldScript = map.get(id.uniqueName);
			if(oldScript != null && !oldScript.getAbsoluteFilePath().equals(absoluteFilePath)){
				throw new IllegalArgumentException("script with "+id+" already exists");
			}
		}
	}


	void removeByPath(String path){
		String absolutePath = new File(path).getAbsolutePath();
		
		writeLock.lock();
		try{
			Script oldByPath = scriptsByAbsolutePath.remove(absolutePath);
			removeFromIdMapIfCan(oldByPath);
		}finally{
			writeLock.unlock();
		}
	}
	
	private void removeFromIdMapIfCan(Script oldByPath) {
		if(oldByPath != null){
			ScriptId id = oldByPath.getId();
			HashMap<Object, Script> map = scriptsById.get(id.domain);
			map.remove(id.uniqueName);
		}
	}
	
	
	public Set<Object> getNamesByDomain(Object domain){
		readLock.lock();
		try{
			HashMap<Object, Script> map = scriptsById.get(domain);
			if(map == null) return new HashSet<Object>(0);
			
			HashSet<Object> out = new HashSet<Object>();
			for(Script script : map.values()){
				out.add(script.getId().uniqueName);
			}
			return out;
		}finally{
			readLock.unlock();
		}
	}
	
	public InstanceDelegate createInstance(Object domain, Object name) throws Exception{
		readLock.lock();
		try{
			HashMap<Object, Script> map = scriptsById.get(domain);
			if(map == null) throw new ScriptNotExistException("no domain "+domain);
			Script script = map.get(name);
			if(script == null) throw new ScriptNotExistException("no name "+name+" in domain "+domain);
			
			Object ob = script.createInstance();
			return new InstanceDelegate(ob,script,this);
		}finally{
			readLock.unlock();
		}
	}
	
	Script getScript(ScriptId id) {
		readLock.lock();
		try{
			HashMap<Object, Script> map = scriptsById.get(id.domain);
			if(map == null) return null;
			return map.get(id.uniqueName);
		}finally{
			readLock.unlock();
		}
	}
	
	
	public int getScriptsCount(){
		readLock.lock();
		try{
			return scriptsByAbsolutePath.size();
		}finally{
			readLock.unlock();
		}
	}
	
	
	public void addListener(ScriptServiceListener listener){
		listeners.add(listener);
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


class RedeployManagerListenerImpl implements RedeployManagerListener{
	
	ScriptsService scriptsService;
	
	public RedeployManagerListenerImpl(ScriptsService scriptsService) {
		super();
		this.scriptsService = scriptsService;
	}

	@Override
	public void onCreate(String path) {
		File file = new File(path);
		if(file.isDirectory()){
			scriptsService.initRecursive(file);
		}else {
			scriptsService.registerScriptRequest(file);
		}
	}
	
	@Override
	public void onUpdate(String path) {
		scriptsService.registerScriptRequest(new File(path));
	}
	
	@Override
	public void onDelete(String path) {
		scriptsService.removeByPath(path);
	}

}
