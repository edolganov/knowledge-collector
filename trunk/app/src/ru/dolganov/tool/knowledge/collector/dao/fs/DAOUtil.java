package ru.dolganov.tool.knowledge.collector.dao.fs;

public class DAOUtil {
	
	public static String getFilePath(String parentDirPath, String fileName) {
		String path = new StringBuilder().append(parentDirPath).append('/').append(fileName).toString();
		return path;
	}

}
