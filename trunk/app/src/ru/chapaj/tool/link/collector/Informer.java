package ru.chapaj.tool.link.collector;

/**
 * Теперь всегда сохраняем файл сразу - информер больше не нужен
 * @author jenua.dolganov
 */

public class Informer {
	
	private String curFileName;
	private boolean fileModified;
	
	public void newFile(String fileName) {
		App.getDefault().getUI().setTitle(App.APP_TITLE+" - "+fileName);
		curFileName = fileName;
		fileModified = false;
	}
	
	public void fileIsModified(){
		if(curFileName != null){
			App.getDefault().getUI().setTitle(App.APP_TITLE+" - *"+curFileName);
			fileModified = true;
		}
	}

	public boolean isFileModified() {
		return fileModified;
	}

}
