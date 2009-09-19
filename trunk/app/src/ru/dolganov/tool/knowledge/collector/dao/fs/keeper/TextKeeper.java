package ru.dolganov.tool.knowledge.collector.dao.fs.keeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import model.knowledge.NodeMeta;
import ru.chapaj.util.Check;
import ru.chapaj.util.collection.SyncHashMap;
import ru.chapaj.util.file.FileUtil;
import ru.dolganov.tool.knowledge.collector.dao.fs.DU;
import ru.dolganov.tool.knowledge.collector.dao.fs.SaveOps;
import ru.dolganov.tool.knowledge.collector.model.HasNodeMetaParams;

public class TextKeeper extends AbstractKeeper implements HasNodeMetaParams{
	
	SyncHashMap<String, String> saveTextCache = new SyncHashMap<String, String>();
	//
	//List<String> mainTextCache = new SyncListWrapper<String>(new ArrayList<String>(10));
	
	public void beforeUpdate(NodeMeta node, Map<String, String> params){
		String text = params.get(Params.text.toString());
		saveTextCache.put(node.getUuid(), text);
	}
	
	private void afterUpdate(NodeMeta node) {
		saveTextCache.remove(node.getUuid());
	}
	
	public void manage(File rootFile, Map<SaveOps, Object[]> ops) {
		String rootPath = rootFile.getPath();
		if(ops.containsKey(SaveOps.rename)){
			Object[] objects = ops.get(SaveOps.rename);
			NodeMeta node = (NodeMeta)objects[0];
			String newName = node.getName();
			String oldName = (String)objects[1];
			
			String newFileName = getTextFileName(newName);
			String oldFileName = getTextFileName(oldName);
			String filePath = DU.getFilePath(rootPath, oldFileName);
			//rename file
			File file = new File(filePath);
			if(file.exists()) file.renameTo(new File(DU.getFilePath(rootPath,newFileName)));
			//rename dir
			String oldDirName = getDirPath(oldName);
			String newDirName = getDirPath(newName);
			File dir = new File(DU.getFilePath(rootPath, oldDirName));
			if(dir.exists()) dir.renameTo(new File(DU.getFilePath(rootPath, newDirName)));
		}
		
		if(ops.containsKey(SaveOps.delete)){
			NodeMeta node = (NodeMeta)ops.get(SaveOps.delete)[0];
			String name = node.getName();
			long timeStamp = System.currentTimeMillis();
			//delete file
			delManager.delete(rootPath, getTextFileName(name),timeStamp);
			//delete dir
			delManager.delete(rootPath, getDirPath(name),timeStamp);
		}
		
		if(ops.containsKey(SaveOps.update)){
			Object[] objects = ops.get(SaveOps.update);
			NodeMeta node = (NodeMeta)objects[0];
			String fileName = getTextFileName(node.getName());
			String text = (String)objects[1];
			String filePath = DU.getFilePath(rootPath, fileName);
			
			String tempFilePath = filePath+".old";
			File tempFile = null;
			File oldFile = new File(filePath);
			if(oldFile.exists()){
				tempFile = new File(tempFilePath);
				oldFile.renameTo(tempFile);
			}
			File file = new File(filePath);
			try {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(text.getBytes("UTF-8"));
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(tempFile != null) tempFile.delete();
			
			afterUpdate(node);
		}
	}



	private String getTextFileName(String name) {
		return DU.convertToValidFSName(name)+".txt";
	}

	public Map<String, Object> getExternalData(NodeMeta node) {
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

	public String getDirPath(NodeMeta meta) {
		return getDirPath(meta.getName());
	}
	
	private String getDirPath(String name) {
		return DU.SYSTEM_CHAR+"note"+DU.SYSTEM_CHAR+DU.convertToValidFSName(name);
	}

}
