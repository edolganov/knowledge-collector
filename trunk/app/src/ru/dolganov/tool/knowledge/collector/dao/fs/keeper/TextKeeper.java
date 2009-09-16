package ru.dolganov.tool.knowledge.collector.dao.fs.keeper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import model.knowledge.NodeMeta;
import ru.dolganov.tool.knowledge.collector.dao.fs.DAOUtil;
import ru.dolganov.tool.knowledge.collector.dao.fs.SaveOps;

public class TextKeeper {
	
	public void manage(File rootFile, Map<SaveOps, Object[]> ops) {
		if(ops.containsKey(SaveOps.rename)){
			Object[] objects = ops.get(SaveOps.rename);
			NodeMeta node = (NodeMeta)objects[0];
			String newFileName = getTextFileName(node.getName());
			String oldFileName = getTextFileName((String)objects[1]);
			String parentPath = rootFile.getPath();
			String filePath = DAOUtil.getFilePath(parentPath, oldFileName);
			File file = new File(filePath);
			file.renameTo(new File(DAOUtil.getFilePath(parentPath,newFileName)));
		}
		else if(ops.containsKey(SaveOps.delete)){
			NodeMeta node = (NodeMeta)ops.get(SaveOps.delete)[0];
			String fileName = getTextFileName(node.getName());
			String filePath = DAOUtil.getFilePath(rootFile.getPath(), fileName);
			File file = new File(filePath);
			file.delete();
		}
		else if(ops.containsKey(SaveOps.update)){
			Object[] objects = ops.get(SaveOps.update);
			NodeMeta node = (NodeMeta)objects[0];
			String fileName = getTextFileName(node.getName());
			String text = (String)objects[1];
			String parentPath = rootFile.getPath();
			String filePath = DAOUtil.getFilePath(parentPath, fileName);
			
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
		}
	}

	private String getTextFileName(String name) {
		return name+".txt";
	}

}
