package ru.chapaj.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileUtil {
	
	public static BufferedReader getFileBufferedReader(File file, String charSet) throws UnsupportedEncodingException, FileNotFoundException{
		return new BufferedReader(new InputStreamReader(new FileInputStream(file), charSet));
	}

}
