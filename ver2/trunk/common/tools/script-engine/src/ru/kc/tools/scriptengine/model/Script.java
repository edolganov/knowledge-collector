package ru.kc.tools.scriptengine.model;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Script {
	
	File file;
	GroovyClassLoader loader;

	public Script(File file,GroovyClassLoader loader) {
		super();
		this.file = file;
		this.loader = loader;
	}

	@Override
	public String toString() {
		return "Script [file=" + file.getPath() + "]";
	}

	public Object createObject() throws Exception {
        String text = readFromFile(file.getPath());
		String fileName = file.getName();
		Class<?> groovyClass = loader.parseClass(text, fileName);
        GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
		return groovyObject;
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
