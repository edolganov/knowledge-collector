package ru.dolganov.tool.knowledge.collector.dao.fs;

public class DAOUtil {
	
	public static String getFilePath(String parentDirPath, String dirName) {
		String path = new StringBuilder().append(parentDirPath).append('/').append(dirName).toString();
		return path;
	}

}
