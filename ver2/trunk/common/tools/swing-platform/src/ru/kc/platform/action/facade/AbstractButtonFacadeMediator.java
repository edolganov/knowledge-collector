package ru.kc.platform.action.facade;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;

public abstract class AbstractButtonFacadeMediator<T extends AbstractButtonFacade> extends AbstractFacadeMediator<T> implements AbstractButtonFacade {
	
	private Icon icon;
	private String toolTipText;
	private boolean selected;
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	@Override
	public void setIcon(Icon icon) {
		this.icon = icon;
		for (AbstractButtonFacade realImpl : realFacadeImpls)
			realImpl.setIcon(icon);
	}

	@Override
	public void setToolTipText(String text) {
		this.toolTipText = text;
		for (AbstractButtonFacade realImpl : realFacadeImpls)
			realImpl.setToolTipText(text);
	}

	@Override
	public void addListener(ActionListener actionListener) {
		listeners.add(actionListener);
		for (AbstractButtonFacade realImpl : realFacadeImpls)
			realImpl.addListener(actionListener);
	}

	@Override
	protected void init(AbstractButtonFacade facade) {
		facade.setIcon(icon);
		facade.setToolTipText(toolTipText);
		facade.setSelected(selected);
		for(ActionListener l : listeners)
			facade.addListener(l);
	}

	@Override
	public boolean isSelected() {
		if(realFacadeImpls.size() > 0){
			return realFacadeImpls.get(0).isSelected();
		}
		else return selected;
	}

	@Override
	public void setSelected(boolean value) {
		this.selected = value;
		for (AbstractButtonFacade realImpl : realFacadeImpls)
			realImpl.setSelected(selected);
	}









}
