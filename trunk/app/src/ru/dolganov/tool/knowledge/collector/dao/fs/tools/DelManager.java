package ru.dolganov.tool.knowledge.collector.dao.fs.tools;

import java.io.File;

import ru.dolganov.tool.knowledge.collector.dao.fs.DU;

public class DelManager {
	
	private static final String DEL_DIR = DU.SYSTEM_CHAR+"del"+DU.SYSTEM_CHAR;
	
	public DelManager() {
	}
	
	public void delete(String parentPath,String fileName) throws Exception{
		delete(parentPath, fileName, System.currentTimeMillis());
	}
	
	public void delete(String parentPath,String fileName,long timestamp) throws Exception{
		String delDirPath = DU.getFilePath(parentPath, DEL_DIR);
		File delDir = new File(delDirPath);
		if(!delDir.mkdir())throw new Exception();
		
		File file = new File(DU.getFilePath(parentPath, fileName));
		if(!file.exists())return;
		String newName = generateDeleteFileName(fileName,timestamp);
		if(!file.renameTo(new File(DU.getFilePath(delDirPath, newName))))
			throw new Exception();
	}
	
	private String generateDeleteFileName(String name, long timestamp) {
		StringBuilder sb = new StringBuilder().append(name).append(DU.SYSTEM_CHAR).append(timestamp);
		return sb.toString();
	}

}
