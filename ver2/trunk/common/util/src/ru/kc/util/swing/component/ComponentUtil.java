package ru.kc.util.swing.component;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class ComponentUtil {
	
	
	public static void addAction(JComponent component, String keyStroke, AbstractAction action){
		KeyStroke ks = KeyStroke.getKeyStroke(keyStroke);
		if(ks == null)
			throw new IllegalArgumentException("invalid key stroke: "+keyStroke);
		Object key = ks+"-"+System.currentTimeMillis();
		component.getActionMap().put(key, action);
		component.getInputMap().put(ks, key);
		
		
	}

}
