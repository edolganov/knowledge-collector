package ru.kc.platform.action;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Icon;

import ru.kc.platform.action.facade.AbstractButtonFacade;

public abstract class AbstractButtonAction extends AbstractAction implements AbstractButtonFacade {
	
	private String group;
	private AbstractButton button;

	public AbstractButtonAction(AbstractButton button) {
		super();
		this.button = button;
	}

	@Override
	public void enabledRequest() {
		button.setEnabled(true);
	}

	@Override
	public void disable() {
		button.setEnabled(false);
	}

	@Override
	public void setIcon(Icon icon) {
		button.setIcon(icon);
	}

	@Override
	public void setToolTipText(String text) {
		button.setToolTipText(text);
	}

	@Override
	public void addListener(ActionListener actionListener) {
		button.addActionListener(actionListener);
	}
	
	@Override
	public AbstractButton getComponent(){
		return button;
	}

	@Override
	public void requestFocus() {
		button.requestFocus();
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public boolean isSelected() {
		return button.isSelected();
	}

	@Override
	public void setSelected(boolean value) {
		button.setSelected(value);
	}
	
	

}
