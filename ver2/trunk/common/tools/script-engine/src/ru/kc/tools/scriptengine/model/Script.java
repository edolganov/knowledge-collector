package ru.kc.tools.scriptengine.model;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ru.kc.tools.scriptengine.ScriptId;
import ru.kc.tools.scriptengine.ScriptServiceController;

public class Script {
	
	private ScriptServiceController controller;
	private File file;
	private GroovyClassLoader loader;
	
	//model
	private Class<?> groovyClass;
	private ScriptId id;



	public Script(File file,GroovyClassLoader loader, ScriptServiceController controller) {
		super();
		this.file = file;
		this.loader = loader;
		this.controller = controller;
	}

	public void init() throws Exception {
        String text = readFromFile(file.getPath());
		String fileName = file.getName();
		groovyClass = loader.parseClass(text, fileName);
		
		id = controller.getId(groovyClass);
		if(id == null)
			throw new IllegalStateException(groovyClass+" has null id");
	}

	public String getAbsoluteFilePath() {
		return file.getAbsolutePath();
	}
	
    public ScriptId getId() {
		return id;
	}

	public Object createInstance() throws Exception {
    	return groovyClass.newInstance();
    }

	@SuppressWarnings("unchecked")
	public <T> T invoke(Object instance, String method, Object... args) {
		if(instance instanceof GroovyObject){
			GroovyObject ob = (GroovyObject) instance;
			return (T) ob.invokeMethod(method, args);
		} else {
			throw new IllegalArgumentException("unknow type of object "+instance);
		}
	}


	@Override
	public String toString() {
		return "Script [id=" + id + ", type=" + groovyClass
				+ ", file=" + file + "]";
	}
	
	


	public static String readFromFile(String path) throws IOException {
        StringBuffer sb = new StringBuffer();
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        return sb.toString();
    }




    
    

	
}
