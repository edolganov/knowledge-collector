package ru.kc.util.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

public class FileUtil {
	

	public static boolean deleteDirRecursive(File dir) {
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirRecursive(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (dir.delete());
	}

	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
	
	
	public static String readFileUTF8(File file) throws IOException{
		return readFile(file, "UTF8");
	}
	
	public static String readFile(File file, String charset) throws IOException{
		InputStreamReader r = null;
		OutputStreamWriter w = null;
				
		try{
			r = new InputStreamReader(new FileInputStream(file), charset);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			w = new OutputStreamWriter(out, charset);
			char[] buff = new char[1024*4];
			int i;
			while((i = r.read(buff))>0){
				w.write(buff, 0, i);
			}
			w.flush();
			return out.toString(charset);
		}finally{
			if(r != null) 
				try {
					r.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			if(w != null) 
				try {
					w.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public static void writeFileUTF8(File file, String text) throws IOException {
		writeFile(file, text, "UTF8");
	}
	
	
	public static void writeFile(File file, String text, String charset) throws IOException{
		InputStreamReader r = null;
		OutputStreamWriter w = null;
				
		try{
			r = new InputStreamReader(new ByteArrayInputStream(text.getBytes("UTF8")), charset);
			w = new OutputStreamWriter(new FileOutputStream(file), charset);
			char[] buff = new char[1024*4];
			int i;
			while((i = r.read(buff))>0){
				w.write(buff, 0, i);
			}
			w.flush();
		}finally{
			if(r != null) 
				try {
					r.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			if(w != null) 
				try {
					w.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

}
