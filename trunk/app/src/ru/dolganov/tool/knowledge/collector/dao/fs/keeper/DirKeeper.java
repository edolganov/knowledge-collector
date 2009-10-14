package ru.dolganov.tool.knowledge.collector.dao.fs.keeper;

import java.io.File;
import java.util.Map;

import model.knowledge.NodeMeta;
import ru.dolganov.tool.knowledge.collector.dao.fs.DU;
import ru.dolganov.tool.knowledge.collector.dao.fs.NodeMetaObjectsCacheImpl;
import ru.dolganov.tool.knowledge.collector.dao.fs.SaveOps;
import ru.dolganov.tool.knowledge.collector.dao.fs.exception.MkDirException;
import ru.dolganov.tool.knowledge.collector.dao.fs.exception.RenameException;

public class DirKeeper extends AbstractKeeper{
	
	
	
	
	public DirKeeper(NodeMetaObjectsCacheImpl cache) {
		super(cache);
	}

	public void manage(File rootFile, Map<SaveOps, Object[]> ops) throws Exception {
		String path = rootFile.getPath();
		if(ops.containsKey(SaveOps.create)){
			NodeMeta node = (NodeMeta)ops.get(SaveOps.create)[0];
			String dirName = getName(node.getName());
			String folderPath = DU.getFilePath(path, dirName);
			if(!new File(folderPath).mkdir())throw new MkDirException(folderPath);
		}
		else if(ops.containsKey(SaveOps.delete)){
			NodeMeta node = (NodeMeta)ops.get(SaveOps.delete)[0];
			String name = getName(node.getName());
			delManager.delete(path, name);
			cache.deleteAllRoots(DU.getFilePath(path, name));
		}
		else if(ops.containsKey(SaveOps.rename)){
			Object[] objects = ops.get(SaveOps.rename);
			NodeMeta node = (NodeMeta)objects[0];
			String newDirName = getName(node.getName());
			String oldDirName = getName((String)objects[1]);
			String parentPath = path;
			String folderPath = DU.getFilePath(parentPath, oldDirName);
			File file = new File(folderPath);
			if(file.exists()){
				String newPath = DU.getFilePath(parentPath,newDirName);
				if(!file.renameTo(new File(newPath)))
					throw new RenameException(folderPath);
				cache.renameAllRoots(folderPath,newPath);
			}
		}
		else if(ops.containsKey(SaveOps.move)){
			NodeMeta node = (NodeMeta)ops.get(SaveOps.move)[0];
			String dirName = getDirName(node);
			String oldPath = DU.getFilePath(path, dirName);
			String newRootPath = node.getParent().getDirPath();
			String newPath = DU.getFilePath(newRootPath, dirName);
			//System.out.println("DirKeeper: " + oldPath + " -> " + newPath);
			File oldFile = new File(oldPath);
			if(oldFile.exists()){
				if(!oldFile.renameTo(new File(newPath)))
					throw new RenameException(oldPath);
				cache.renameAllRoots(oldPath,newPath);
			}
		}
	}
	
	private String getName(String name){
		return DU.convertToValidFSName(name);
	}

	public String getDirName(NodeMeta meta) {
		return getName(meta.getName());
	}

}
