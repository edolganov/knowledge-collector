package ru.kc.util.swing.text;

import java.io.IOException;
import java.io.StringReader;

import javax.swing.text.JTextComponent;

public class TextComponentUtil {
	
	public static void setTextWithSaveCaretPosition(JTextComponent component, String text){
		int oldCaretPosition = component.getCaretPosition();
		component.setText(text);
		if(component.isFocusOwner()){
			component.setCaretPosition(oldCaretPosition);
		} else {
			component.setCaretPosition(0);
		}
	}
	
	public static void readTextWithSaveCaretPosition(JTextComponent component, String text, String type) throws IOException{
		int oldCaretPosition = component.getCaretPosition();
		component.read(new StringReader(text), type);
		if(component.isFocusOwner()){
			component.setCaretPosition(oldCaretPosition);
		} else {
			component.setCaretPosition(0);
		}
	}

}
