package ru.kc.module.properties.tools;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import ru.kc.util.Check;

public class EmptyTextAreaDecorator {
	
	JTextComponent textArea;
	Color initBackground;
	
	public EmptyTextAreaDecorator(JTextComponent textArea) {
		this.textArea = textArea;
		initBackground = textArea.getBackground();
		
		textArea.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusGained(FocusEvent e) {
				setNotEmptyBackground();
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				fillBackground();
			}
		});
	}


	public void fillBackground() {
		String text = textArea.getText();
		if(Check.isEmpty(text)){
			setEmptyBackground();
		} else {
			setNotEmptyBackground();
		}
	}
	
	protected void setEmptyBackground(){
		Container parent = textArea.getParent();
		if(parent != null){
			Color emptyColor = new Color(UIManager.getDefaults().getColor("Panel.background").getRGB());
			textArea.setBackground(emptyColor);
		}
	}
	
	protected void setNotEmptyBackground() {
		textArea.setBackground(initBackground);
	}

}
