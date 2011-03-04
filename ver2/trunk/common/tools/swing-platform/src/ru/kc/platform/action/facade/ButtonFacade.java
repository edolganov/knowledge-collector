package ru.kc.platform.action.facade;

import java.awt.event.ActionListener;

import javax.swing.Icon;

public interface ButtonFacade extends AbstractActionFacade {
	
	void setIcon(Icon icon);
	
	void setToolTipText(String text);
	
	void addListener(ActionListener actionListener);
	

}
