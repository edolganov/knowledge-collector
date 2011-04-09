package ru.kc.module.imports.oldclient.model;

public class ImportOldDataTextModel {
	
	
	public String title = "";
	private String text = "";
	
	
	public String getText(){
		StringBuilder sb = new StringBuilder();
		sb.append(title);
		sb.append(text);
		return sb.toString();
	}


	public void addText(String newString) {
		text = text+"\n"+newString;
	}

}
