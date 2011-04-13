package ru.kc.util.swing.os;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import ru.kc.util.Check;

public class OSUtil {
	
	public static void openUrl(String url) throws IOException, URISyntaxException{
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(new URI(url));
	}

	public static void openFile(String url) throws IOException {
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows")){
			openWinFile(url);
		} else {
			Desktop desktop = Desktop.getDesktop();
			desktop.open(new File(url));
		}
		
	}
	
	
	public static boolean openWinFile(String filePath){
		try{
			filePath = filePath.trim();
			String path = null;
			String file = null;
			String params = null;
			boolean allDone = false;
			//may be path="e:\my doc" -param1
			if('\"' == filePath.charAt(0)){
				int unquote=filePath.lastIndexOf('\"');
				if(unquote != -1){
					String fullPath = filePath;
					filePath = fullPath.substring(1, unquote);
					params = fullPath.substring(unquote+1);
					
					//clean path
					if(filePath.endsWith("\\") || filePath.endsWith("/")) {
						filePath = filePath.substring(0, filePath.length()-1) ;
					}
					int i = filePath.lastIndexOf('\\');
					if(i == -1) i = filePath.lastIndexOf("/");
					if(i == -1) return false;
					path = filePath.substring(0, i);
					file = filePath.substring(path.length()+1);
					
					allDone = true;
				}
			}
			if(!allDone){
				int blank = filePath.indexOf(' ');
				if(blank != -1) {
					params = filePath.substring(blank);
					filePath = filePath.substring(0,filePath.length()-params.length());
				}
				
				//clean path
				if(filePath.endsWith("\\") || filePath.endsWith("/")) {
					filePath = filePath.substring(0, filePath.length()-1) ;
				}
				int i = filePath.lastIndexOf('\\');
				if(i == -1) i = filePath.lastIndexOf("/");
				if(i == -1) return false;
				path = filePath.substring(0, i);
				file = filePath.substring(path.length()+1);
			}
			
			String executeExpression = "cmd /c start \""+filePath+"\" /D \""+path+"\" \""+file+"\"";
			if(!Check.isEmpty(params)){
				if(' ' != params.charAt(0)) params = ' '+params;
				executeExpression += params;
			}
			//System.out.println(executeExpression);
			//ProcessWrapper.exec(executeExpression);
			Runtime.getRuntime().exec(executeExpression);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
