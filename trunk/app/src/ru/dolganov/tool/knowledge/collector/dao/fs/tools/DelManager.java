package ru.dolganov.tool.knowledge.collector.dao.fs.tools;

import java.io.File;

import ru.dolganov.tool.knowledge.collector.dao.fs.DU;

public class DelManager {
	
	private static final String DEL_DIR = DU.SYSTEM_CHAR+"del"+DU.SYSTEM_CHAR;
	
	public DelManager() {
	}
	
	public void delete(String parentPath,String fileName){
		delete(parentPath, fileName, System.currentTimeMillis());
	}
	
	public void delete(String parentPath,String fileName,long timestamp){
		String delDirPath = DU.getFilePath(parentPath, DEL_DIR);
		File delDir = new File(delDirPath);
		delDir.mkdir();
		
		File file = new File(DU.getFilePath(parentPath, fileName));
		String newName = generateDeleteFileName(fileName,timestamp);
		file.renameTo(new File(DU.getFilePath(delDirPath, newName)));
	}
	
	private String generateDeleteFileName(String name, long timestamp) {
		StringBuilder sb = new StringBuilder().append(name).append(DU.SYSTEM_CHAR).append(timestamp);
		return sb.toString();
	}

}
