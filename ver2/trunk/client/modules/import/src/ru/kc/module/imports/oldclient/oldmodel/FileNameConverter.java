package ru.kc.module.imports.oldclient.oldmodel;

import java.util.Arrays;


public class FileNameConverter {
	
	public static final char[] correctFileNameChars = initChars();
	
	public static final char SYSTEM_CHAR = '`';
	
	private static char[] initChars(){
		//do not contains SYSTEM_CHAR
		char[] correctChars = new char[] { 'a',
				'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',

				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',

				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

				'.', '_',' ','!','+',
				'(', ')', '-', '{', '}', '[', ']',

				'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё','Ж', 'З', 'И', 'Й', 'К', 'Л', 'М',
				'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ',
				'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 
				
				'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж',
				'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
				'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'

		};
		Arrays.sort(correctChars);
		return correctChars;
	}
	
	private static final int approxiMatemaxSize = 40;
	

	public static String convertToValidFSName(String name){
		int length = name.length();
		int count = length;
		if(length > approxiMatemaxSize){
			count = approxiMatemaxSize;
		}
		
		StringBuilder sb = new StringBuilder(approxiMatemaxSize+20);
		char ch;
		for (int i = 0; i < count; i++) {
			ch = name.charAt(i);
			if(Arrays.binarySearch(correctFileNameChars,ch) > -1){
				sb.append(ch);
			}
			else {
				sb.append((int)ch);
			}
		}
		if(count < length){
			sb.append(SYSTEM_CHAR);
			int suffix = name.substring(count+1).hashCode();
			sb.append(suffix);
		}
		return sb.toString();
	}

}
