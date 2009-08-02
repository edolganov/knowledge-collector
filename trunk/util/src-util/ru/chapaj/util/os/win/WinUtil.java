package ru.chapaj.util.os.win;

import ru.chapaj.util.os.ProcessWrapper;

public class WinUtil {
	
	public static boolean openFile(String filePath){
		try{
			if(filePath.endsWith("\\") || filePath.endsWith("/")) {
				filePath = filePath.substring(0, filePath.length()-1) ;
			}
			filePath.lastIndexOf('\\');
			int i = filePath.lastIndexOf('\\');
			if(i == -1) i = filePath.lastIndexOf("/");
			if(i == -1) return false;
			
			String path = filePath.substring(0, i);
			
			String file = filePath.substring(path.length()+1);
			
			ProcessWrapper.exec("cmd /c start \" \" /D\""+path+"\" \""+file+"\"");
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
