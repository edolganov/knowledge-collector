package ru.kc.platform.scripts.model;

import groovy.lang.GroovyClassLoader;

import java.io.File;

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

	public Object createObject() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
