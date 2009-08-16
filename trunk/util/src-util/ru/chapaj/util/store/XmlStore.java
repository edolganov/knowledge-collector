package ru.chapaj.util.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.chapaj.util.file.FileUtil;
import ru.chapaj.util.xml.ObjectToXMLConverter;

public abstract class XmlStore<T> {
	
	private ObjectToXMLConverter<T> converter;
	
	
	public XmlStore() {
		converter = new ObjectToXMLConverter<T>(false);
		config(converter);
	}
	
	
	public void saveFile(File file, T data, boolean saveOldFile) throws IOException{
		File savingFile;
		boolean usingTempFile = true;
		if(file.exists()){
			savingFile = new File(file.getPath()+".tmp");
			savingFile.delete();
		}
		else {
			usingTempFile = false;
			savingFile = file;
		}
		savingFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(savingFile);
        fos.write(converter.toXMLString(data).getBytes("UTF-8"));
        fos.flush();
        fos.close();
        if(usingTempFile){
        	String path = file.getPath();
        	String oldPath = path + ".old";
        	File oldFile = new File(oldPath);
        	oldFile.delete();
        	file.renameTo(oldFile);
        	savingFile.renameTo(new File(path));
        	if(! saveOldFile){
        		oldFile.delete();
        	}
        }
//	        log.info("Saved: "+file.getAbsolutePath());
	}
	
	
	public T loadFile(File file) throws IOException{
		BufferedReader input = null;
		try{
    	  	input = FileUtil.getFileBufferedReader(file, "UTF-8");
		}
		catch (IOException e) {
			throw e;
		}
  		try {
  			String line = null;
  			StringBuffer sb = new StringBuffer();
  			while ((line = input.readLine()) != null) {
  				sb.append(line);
  				sb.append('\n');
  			}
  			return converter.toObject(sb.toString());

  		} finally {
  			input.close();
  		}
	}

	protected abstract void config(ObjectToXMLConverter<T> converter);

}
