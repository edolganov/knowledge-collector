package ru.kc.platform.action.facade;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;

public class ButtonFacadeMediator extends AbstractFacadeMediator<ButtonFacade> implements ButtonFacade {
	
	private Icon icon;
	private String toolTipText;
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	@Override
	public void setIcon(Icon icon) {
		this.icon = icon;
		for (ButtonFacade realImpl : realFacadeImpls)
			realImpl.setIcon(icon);
	}

	@Override
	public void setToolTipText(String text) {
		this.toolTipText = text;
		for (ButtonFacade realImpl : realFacadeImpls)
			realImpl.setToolTipText(text);
	}

	@Override
	public void addListener(ActionListener actionListener) {
		listeners.add(actionListener);
		for (ButtonFacade realImpl : realFacadeImpls)
			realImpl.addListener(actionListener);
	}

	@Override
	protected void init(ButtonFacade facade) {
		facade.setIcon(icon);
		facade.setToolTipText(toolTipText);
		for(ActionListener l : listeners)
			facade.addListener(l);
	}









}
