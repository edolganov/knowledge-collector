package ru.chapaj.util.os.win;

import ru.chapaj.util.Check;
import ru.chapaj.util.os.ProcessWrapper;

public class WinUtil {
	
	//test
//	public static void main(String[] args) {
//		//openFile("\"E:\\chapaj\\programs\\_developing\\eclipse-3.5\\eclipse.exe\" -data 'E:\\chapaj\\work\\projects'");
//		//openFile("\"E:\\chapaj\\programs\\_developing\\eclipse-3.5\\eclip se.exe\" -data 'E:\\chapaj\\work\\projects'");
//		openFile("E:\\chapaj\\programs\\_developing\\eclipse-3.5\\eclipse.exe -data 'E:\\chapaj\\work\\projects'");
//		//openFile("E:\\chapaj\\programs\\_developing\\eclipse-3.5\\ecl ipse.exe");
//	}
	
	public static boolean openFile(String filePath){
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
			ProcessWrapper.exec(executeExpression);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
