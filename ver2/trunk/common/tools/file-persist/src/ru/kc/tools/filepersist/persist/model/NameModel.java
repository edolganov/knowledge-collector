package ru.kc.tools.filepersist.persist.model;

public class NameModel {
	
	public String first() {
		return first(null);
	}

	public String first(String ext) {
		if(ext == null) ext = "";
		return "000"+ext;
	}

	public String next(String name) {
		String fileName = null;
		String fileExt = null;
		int dotIndex = name.lastIndexOf('.');
		if(dotIndex > -1){
			fileName = name.substring(0, dotIndex);
			fileExt = name.substring(dotIndex);
		} else {
			fileName = name;
			fileExt = "";
		}
		
		int parseInt = Integer.parseInt(fileName);
		parseInt++;
		String newName = Integer.toString(parseInt);
		int length = newName.length();
		if(length == 1)newName = "00"+newName;
		else if(length == 2)newName = "0"+newName;
		
		return newName+fileExt;
	}

}
