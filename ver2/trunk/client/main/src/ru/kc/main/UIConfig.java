package ru.kc.main;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ru.kc.util.swing.config.ObjectHandler;
import ru.kc.util.swing.laf.Laf;

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
		else if(ob instanceof JButton){
			Laf.setFocusForLeftRightArrowKeys((JButton)ob);
		}
		else if(ob instanceof JTextField){
			Laf.setFocusForUpDownArrowKeys((JTextField)ob);
		}
		
	}

}
