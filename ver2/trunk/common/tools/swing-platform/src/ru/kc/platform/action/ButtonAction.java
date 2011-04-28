package ru.kc.platform.action;

import javax.swing.JButton;

import ru.kc.platform.action.facade.ButtonFacade;

public class ButtonAction extends AbstractButtonAction implements ButtonFacade {

	public ButtonAction() {
		super(new JButton());
	}

}
