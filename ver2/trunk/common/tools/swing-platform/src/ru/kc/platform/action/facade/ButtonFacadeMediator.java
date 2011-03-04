package ru.kc.platform.action.facade;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;

public class ButtonFacadeMediator implements ButtonFacade {
	
	private Icon icon;
	private String toolTipText;
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	private List<ButtonFacade> realFacadeImpls = Collections.emptyList();

	@Override
	public void enabledRequest() {
		for (ButtonFacade realImpl : realFacadeImpls)
			realImpl.enabledRequest();
	}

	@Override
	public void disable() {
		for (ButtonFacade realImpl : realFacadeImpls)
			realImpl.disable();
	}

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
	
	public void addRealFacade(ButtonFacade facade){
		realFacadeImpls = new ArrayList<ButtonFacade>(realFacadeImpls);
		init(facade);
		realFacadeImpls.add(facade);
	}

	private void init(ButtonFacade facade) {
		facade.setIcon(icon);
		facade.setToolTipText(toolTipText);
		for(ActionListener l : listeners)
			facade.addListener(l);
	}

}
