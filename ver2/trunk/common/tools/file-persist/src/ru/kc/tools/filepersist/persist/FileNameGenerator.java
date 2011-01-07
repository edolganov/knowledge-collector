package ru.kc.tools.filepersist.persist;

import java.io.File;

public class FileNameGenerator {
	
	File rootDir;

	public FileNameGenerator(File rootDir) {
		this.rootDir = rootDir;
	}

	public File getFirstFolder() {
		return new File(rootDir, "0000");
	}
	
	public File getFirstFile(){
		File firstFolder = getFirstFolder();
		return new File(firstFolder,"0000.xml");
	}

}
