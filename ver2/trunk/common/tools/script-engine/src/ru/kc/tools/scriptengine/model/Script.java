package ru.kc.tools.scriptengine.model;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ru.kc.tools.scriptengine.ScriptServiceController;

public class Script {
	
	private ScriptServiceController controller;
	private File file;
	private GroovyClassLoader loader;
	
	//model
	private Object mapping;
	private Class<?> groovyClass;



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
		
		mapping = controller.getMapping(groovyClass);
		if(mapping == null)
			throw new IllegalStateException(groovyClass+" has null mapping");
	}

	public File getFile() {
		return file;
	}
	
    public Object getMapping() {
		return mapping;
	}
    
    public String getType() {
		return groovyClass.getName();
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
		return "Script [mapping=" + mapping + ", type=" + groovyClass
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
