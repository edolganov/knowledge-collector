package ru.kc.tools.scriptengine;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	
	//model
	private ReadWriteLock rw = new ReentrantReadWriteLock();
	private Lock readLock = rw.readLock();
	private Lock writeLock = rw.writeLock();
	private HashMap<Object, HashMap<String,Script>> scriptsByMapping = new HashMap<Object, HashMap<String,Script>>();
	private HashMap<String, Script> scriptsByAbsolutePath = new HashMap<String, Script>();
	
	public ScriptsService(ScriptServiceController serviceController) {
		this.serviceController = serviceController;
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
		Script script = new Script(file,loader,serviceController);
		log.info("Registering: " + script);
		
		try {
			script.init();
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
			removeFromMapping(oldByPath);
			
			Object mapping = script.getMapping();
			HashMap<String, Script> map = scriptsByMapping.get(mapping);
			if(map == null){
				map = new HashMap<String, Script>();
				scriptsByMapping.put(mapping, map);
			}
			map.put(script.getType(), script);

		}finally{
			writeLock.unlock();
		}
	}
	
	void removeByPath(String path){
		String absolutePath = new File(path).getAbsolutePath();
		
		writeLock.lock();
		try{
			Script oldByPath = scriptsByAbsolutePath.remove(absolutePath);
			removeFromMapping(oldByPath);

		}finally{
			writeLock.unlock();
		}
	}
	
	private void removeFromMapping(Script oldByPath) {
		if(oldByPath != null){
			Object mapping = oldByPath.getMapping();
			String type = oldByPath.getType();
			HashMap<String, Script> map = scriptsByMapping.get(mapping);
			map.remove(type);
		}
	}
	
	
	public List<String> getTypes(Object mapping){
		readLock.lock();
		try{
			HashMap<String, Script> map = scriptsByMapping.get(mapping);
			if(map == null) return new ArrayList<String>(0);
			
			ArrayList<String> out = new ArrayList<String>();
			for(Script script : map.values()){
				out.add(script.getType());
			}
			return out;
		}finally{
			readLock.unlock();
		}
	}
	
	public InstanceDelegate createInstance(Object mapping, String type) throws Exception{
		readLock.lock();
		try{
			InstanceDelegate nullInstance = new InstanceDelegate(null,mapping,type, this);
			
			HashMap<String, Script> map = scriptsByMapping.get(mapping);
			if(map == null) return nullInstance;
			Script script = map.get(type);
			if(script == null) return nullInstance;
			
			Object ob = script.createInstance();
			return new InstanceDelegate(ob,mapping,type,this);
		}finally{
			readLock.unlock();
		}
	}
	
	Script getScript(Object mapping, String type) {
		readLock.lock();
		try{
			HashMap<String, Script> map = scriptsByMapping.get(mapping);
			if(map == null) return null;
			return map.get(type);
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
