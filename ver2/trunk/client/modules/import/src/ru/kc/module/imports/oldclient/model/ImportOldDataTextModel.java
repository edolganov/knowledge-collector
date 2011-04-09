package ru.kc.module.imports.oldclient.model;

public class ImportOldDataTextModel {
	
	
	public String title = "";
	public String searchTitle = "";
	
	private char newLine = '\n';
	
	
	public String getText(){
		StringBuilder sb = new StringBuilder();
		sb.append(title);
		sb.append(newLine);
		sb.append(searchTitle);
		return sb.toString();
	}

}
