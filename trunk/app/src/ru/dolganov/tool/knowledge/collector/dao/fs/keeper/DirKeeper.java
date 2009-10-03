package ru.dolganov.tool.knowledge.collector.dao.fs.keeper;

import java.io.File;
import java.util.Map;

import model.knowledge.NodeMeta;
import ru.dolganov.tool.knowledge.collector.dao.fs.DU;
import ru.dolganov.tool.knowledge.collector.dao.fs.SaveOps;

public class DirKeeper extends AbstractKeeper{
	
	
	public void manage(File rootFile, Map<SaveOps, Object[]> ops) {
		if(ops.containsKey(SaveOps.create)){
			NodeMeta node = (NodeMeta)ops.get(SaveOps.create)[0];
			String dirName = getName(node.getName());
			String folderPath = DU.getFilePath(rootFile.getPath(), dirName);
			new File(folderPath).mkdir();
		}
		else if(ops.containsKey(SaveOps.delete)){
			NodeMeta node = (NodeMeta)ops.get(SaveOps.delete)[0];
			delManager.delete(rootFile.getPath(), getName(node.getName()));
		}
		else if(ops.containsKey(SaveOps.rename)){
			Object[] objects = ops.get(SaveOps.rename);
			NodeMeta node = (NodeMeta)objects[0];
			String newDirName = getName(node.getName());
			String oldDirName = getName((String)objects[1]);
			String parentPath = rootFile.getPath();
			String folderPath = DU.getFilePath(parentPath, oldDirName);
			File file = new File(folderPath);
			file.renameTo(new File(DU.getFilePath(parentPath,newDirName)));
		}
		else if(ops.containsKey(SaveOps.move)){
			NodeMeta node = (NodeMeta)ops.get(SaveOps.move)[0];
			String dirName = getDirName(node);
			String oldPath = DU.getFilePath(rootFile.getPath(), dirName);
			String newRootPath = node.getParent().getDirPath();
			String newPath = DU.getFilePath(newRootPath, dirName);
			//System.out.println("DirKeeper: " + oldPath + " -> " + newPath);
			new File(oldPath).renameTo(new File(newPath));
		}
	}
	
	private String getName(String name){
		return DU.convertToValidFSName(name);
	}

	public String getDirName(NodeMeta meta) {
		return getName(meta.getName());
	}

}
