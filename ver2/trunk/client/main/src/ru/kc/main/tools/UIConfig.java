package ru.kc.main.tools;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import ru.kc.util.swing.config.ObjectHandler;
import ru.kc.util.swing.laf.Laf;

public class UIConfig extends ObjectHandler {

	@Override
	public void process(Object ob) {
		
		if(ob instanceof JSplitPane){
			JSplitPane p = ((JSplitPane)ob);
			p.setDividerSize(2);
		}
		else if(ob instanceof RSyntaxTextArea){
			((RSyntaxTextArea) ob).setFont(new Font("Arial", Font.PLAIN, 12));
		}
		else if(ob instanceof JTextArea){
			((JTextArea) ob).setFont(new JLabel().getFont());
		}
		else if(ob instanceof JButton 
				|| ob instanceof JToggleButton){
			Laf.setFocusForLeftRightArrowKeys((Component)ob);
			Laf.setFocusForUpDownArrowKeys((Component)ob);
		}
		else if(ob instanceof JTextField){
			Laf.setFocusForUpDownArrowKeys((JTextField)ob);
		}
		
	}

}
