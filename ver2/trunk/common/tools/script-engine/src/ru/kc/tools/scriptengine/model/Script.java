package ru.kc.tools.scriptengine.model;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ru.kc.tools.scriptengine.model.annotations.Mapping;

public class Script {
	
	private File file;
	private GroovyClassLoader loader;
	private GroovyObject groovyObject;
	private String mappingValue;

	public Script(File file,GroovyClassLoader loader) {
		super();
		this.file = file;
		this.loader = loader;
	}

	public void createScript() throws Exception {
        String text = readFromFile(file.getPath());
		String fileName = file.getName();
		Class<?> groovyClass = loader.parseClass(text, fileName);

	    groovyObject = (GroovyObject) groovyClass.newInstance();
		
		Mapping mapping = groovyObject.getClass().getAnnotation(Mapping.class);
		if(mapping == null)
			throw new IllegalStateException(groovyObject+" doesn't have @Mapping annotation");
		
		
		mappingValue = mapping.value();
		if(mappingValue == null)
			throw new IllegalStateException(groovyObject+" have null mapping");
        
	}
	
	public File getFile() {
		return file;
	}
	
    public String getMapping() {
		return mappingValue;
	}

	@Override
	public String toString() {
		return "Script [mappingValue=" + mappingValue + ", file=" + file + "]";
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

	@SuppressWarnings("unchecked")
	public <T> T invoke(String method, Object... args) {
		return (T) groovyObject.invokeMethod(method, args);
	}


    
    

	
}
