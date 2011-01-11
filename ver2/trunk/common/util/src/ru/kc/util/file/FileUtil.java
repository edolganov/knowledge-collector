package ru.kc.util.file;

import java.io.File;

public class FileUtil {
	
	public static boolean deleteDirRecursive(File dir){
	    if( dir.exists() ) {
	        File[] files = dir.listFiles();
	        for(int i=0; i<files.length; i++) {
	           if(files[i].isDirectory()) {
	        	   deleteDirRecursive(files[i]);
	           }
	           else {
	             files[i].delete();
	           }
	        }
	      }
	      return( dir.delete() );
	}

}
