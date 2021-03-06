package ru.dolganov.tool.knowledge.collector.dao.fs.keeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import model.knowledge.Node;
import model.knowledge.RootElement;
import ru.chapaj.util.Check;
import ru.chapaj.util.collection.SyncHashMap;
import ru.chapaj.util.file.FileUtil;
import ru.dolganov.tool.knowledge.collector.dao.fs.DU;
import ru.dolganov.tool.knowledge.collector.dao.fs.NodeMetaObjectsCacheImpl;
import ru.dolganov.tool.knowledge.collector.dao.fs.SaveOps;
import ru.dolganov.tool.knowledge.collector.dao.fs.exception.DeleteException;
import ru.dolganov.tool.knowledge.collector.dao.fs.exception.RenameException;
import ru.dolganov.tool.knowledge.collector.model.HasNodeMetaParams;

public class TextKeeper extends AbstractKeeper implements HasNodeMetaParams{
	
	SyncHashMap<String, String> saveTextCache = new SyncHashMap<String, String>();
	//
	//List<String> mainTextCache = new SyncListWrapper<String>(new ArrayList<String>(10));
	
	
	
	public TextKeeper(NodeMetaObjectsCacheImpl cache) {
		super(cache);
	}

	public void beforeUpdate(RootElement node, Map<String, String> params){
		String text = params.get(Params.text.toString());
		saveTextCache.put(node.getUuid(), text);
	}
	
	private void afterUpdate(RootElement node) {
		saveTextCache.remove(node.getUuid());
	}
	
	public void manage(File rootFile, Map<SaveOps, Object[]> ops) throws Exception {
		String rootPath = rootFile.getPath();
		if(ops.containsKey(SaveOps.rename)){
			Object[] objects = ops.get(SaveOps.rename);
			Node node = (Node)objects[0];
			String newName = node.getName();
			String oldName = (String)objects[1];
			
			String newFileName = getTextFileName(newName);
			String oldFileName = getTextFileName(oldName);
			String filePath = DU.getFilePath(rootPath, oldFileName);
			//rename file
			File file = new File(filePath);
			if(file.exists()) {
				String newPath = DU.getFilePath(rootPath,newFileName);
				if(!file.renameTo(new File(newPath)))
				throw new RenameException(filePath);
			}
			//rename dir
			String oldDirName = getDirName(oldName);
			String newDirName = getDirName(newName);
			String oldDirPath = DU.getFilePath(rootPath, oldDirName);
			File dir = new File(oldDirPath);
			if(dir.exists()) {
				String newPath = DU.getFilePath(rootPath, newDirName);
				if(!dir.renameTo(new File(newPath)))
					throw new RenameException(dir.getPath());
				cache.renameAllRoots(oldDirPath, newPath);
			}
				
		}
		
		if(ops.containsKey(SaveOps.delete)){
			Node node = (Node)ops.get(SaveOps.delete)[0];
			String name = node.getName();
			long timeStamp = System.currentTimeMillis();
			//delete file
			delManager.delete(rootPath, getTextFileName(name),timeStamp);
			//delete dir
			String dirName = getDirName(name);
			delManager.delete(rootPath, dirName,timeStamp);
			String dirPath = node.getParent().getDirPath();
			cache.deleteAllRoots(DU.getFilePath(dirPath, dirName));
		}
		
		if(ops.containsKey(SaveOps.update)){
			Object[] objects = ops.get(SaveOps.update);
			Node node = (Node)objects[0];
			String fileName = getTextFileName(node.getName());
			String text = (String)objects[1];
			String filePath = DU.getFilePath(rootPath, fileName);
			
			String tempFilePath = filePath+".old";
			File tempFile = null;
			File oldFile = new File(filePath);
			if(oldFile.exists()){
				tempFile = new File(tempFilePath);
				if(!oldFile.renameTo(tempFile))
					throw new RenameException(filePath);
			}
			if(!Check.isEmpty(text)){
				File file = new File(filePath);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(text.getBytes("UTF-8"));
				fos.flush();
				fos.close();
			}
			if(tempFile != null) if(!tempFile.delete())
				throw new DeleteException(tempFile.getPath());
			
			afterUpdate(node);
		}
		
		if(ops.containsKey(SaveOps.move)){
			Node node = (Node)ops.get(SaveOps.move)[0];
			String name = node.getName();
			String dirName = getDirName(name);
			String textName = getTextFileName(name);
			String oldDirPath = DU.getFilePath(rootFile.getPath(), dirName);
			String oldTextPath = DU.getFilePath(rootFile.getPath(), textName);
			String newRootPath = node.getParent().getDirPath();
			String newDirPath = DU.getFilePath(newRootPath, dirName);
			String newTextPath = DU.getFilePath(newRootPath, textName);
			//System.out.println("TextKeeper: " + oldDirPath + " -> " + newDirPath);
			//System.out.println("TextKeeper: " + oldTextPath + " -> " + newTextPath);
			File oldTextFile = new File(oldTextPath);
			if(oldTextFile.exists()){
				if(!oldTextFile.renameTo(new File(newTextPath)))
					throw new RenameException(oldTextPath);
			}
			File oldDir = new File(oldDirPath);
			if(oldDir.exists()){
				if(!oldDir.renameTo(new File(newDirPath)))
					throw new RenameException(oldDirPath);
				cache.renameAllRoots(oldDirPath, newDirPath);
			}
		}
	}


	public Map<String, Object> getExternalData(Node node) {
		HashMap<String, Object> out = new HashMap<String, Object>(1);
		String text = saveTextCache.get(node.getUuid());
		if(text == null){
			String name = node.getName();
			String fileName = getTextFileName(name);
			String dirPath = node.getParent().getDirPath();
			File file = new File(DU.getFilePath(dirPath, fileName));
			if(file.exists()){
				BufferedReader reader = null;
				try {
					reader = FileUtil.getFileBufferedReader(file, "UTF-8");
		  			StringBuffer sb = new StringBuffer();
		  			String line = null;
		  			while ((line = reader.readLine()) != null) {
		  				sb.append(line);
		  				sb.append('\n');
		  			}
		  			text = sb.toString();
		  			reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(!Check.isEmpty(text)){
			out.put(Params.text.toString(), text);
		}
		return out;
	}

	public String getDirName(Node meta) {
		return getDirName(meta.getName());
	}
	
	private String getDirName(String name) {
		return DU.SYSTEM_CHAR+"note"+DU.SYSTEM_CHAR+DU.convertToValidFSName(name);
	}
	
	private String getTextFileName(String name) {
		return DU.convertToValidFSName(name)+".txt";
	}

}
