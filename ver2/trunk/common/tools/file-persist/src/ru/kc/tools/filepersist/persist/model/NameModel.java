package ru.kc.tools.filepersist.persist.model;

public class NameModel {
	
	public String getFirstName() {
		return first(null);
	}

	public String first(String ext) {
		if(ext == null) ext = "";
		return "000"+ext;
	}

	public String next(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
