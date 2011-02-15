package ru.kc.main;

import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import ru.kc.util.swing.config.ObjectHandler;

public class UIConfig extends ObjectHandler {

	@Override
	public void process(Object ob) {
		
		if(ob instanceof JSplitPane){
			JSplitPane p = ((JSplitPane)ob);
			p.setDividerSize(2);
		}
		else if(ob instanceof JTextArea){
			((JTextArea) ob).setFont(new JLabel().getFont());
		}
		
	}

}
