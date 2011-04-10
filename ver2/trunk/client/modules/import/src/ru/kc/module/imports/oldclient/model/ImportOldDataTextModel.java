package ru.kc.module.imports.oldclient.model;

public class ImportOldDataTextModel {
	
	
	private String title = "Importing...";
	private String text = "";
	
	
	public String getText(){
		String curText = this.text;
		
		StringBuilder sb = new StringBuilder();
		sb.append(title);
		sb.append(curText);
		return sb.toString();
	}


	public void addText(String text) {
		String oldText = this.text;
		String newText = oldText+"\n"+text;
		this.text = newText;
	}


	public void errorEnd(String text) {
		addText("Error! "+text);
		addText("\nIMPORT FAILED");
		addText("You can close dialog window");
	}


	public void successEnd() {
		addText("\nIMPORT DONE");
		addText("You can close dialog window");
	}

}
