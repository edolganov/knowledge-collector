package ru.dolganov.tool.knowledge.collector.dao.fs.keeper;

import java.io.File;
import java.util.Map;

import model.knowledge.NodeMeta;
import ru.dolganov.tool.knowledge.collector.dao.fs.DAOUtil;
import ru.dolganov.tool.knowledge.collector.dao.fs.SaveOps;

public class DirKeeper {
	
	private static final String DEL_PREFFIX = "#del#";
	
	public void manage(File rootFile, Map<SaveOps, Object[]> ops) {
		if(ops.containsKey(SaveOps.create)){
			NodeMeta node = (NodeMeta)ops.get(SaveOps.create)[0];
			String dirName = node.getName();
			String folderPath = DAOUtil.getFilePath(rootFile.getPath(), dirName);
			new File(folderPath).mkdir();
		}
		else if(ops.containsKey(SaveOps.delete)){
			NodeMeta node = (NodeMeta)ops.get(SaveOps.delete)[0];
			String dirName = node.getName();
			String folderPath = DAOUtil.getFilePath(rootFile.getPath(), dirName);
			File file = new File(folderPath);
			if(file.isDirectory()){
				String newName = generateDeleteFileName(dirName);
				file.renameTo(new File(DAOUtil.getFilePath(rootFile.getPath(), newName)));
			}
		}
		else if(ops.containsKey(SaveOps.rename)){
			Object[] objects = ops.get(SaveOps.rename);
			NodeMeta node = (NodeMeta)objects[0];
			String newDirName = node.getName();
			String oldDirName = (String)objects[1];
			String parentPath = rootFile.getPath();
			String folderPath = DAOUtil.getFilePath(parentPath, oldDirName);
			File file = new File(folderPath);
			file.renameTo(new File(DAOUtil.getFilePath(parentPath,newDirName)));
		}
	}
	
	private String generateDeleteFileName(String name) {
		StringBuilder sb = new StringBuilder().append(DEL_PREFFIX).append(name).append('-').append(System.currentTimeMillis());
		return sb.toString();
	}

}
