package ru.kc.platform.action;

import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import ru.kc.platform.action.facade.ButtonFacade;

public class ButtonAction extends AbstractAction implements ButtonFacade{
	
	private JButton button = new JButton();

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
	public JButton getComponent(){
		return button;
	}

}
