package ru.kc.platform.action;

import javax.swing.JToggleButton;

import ru.kc.platform.action.facade.ToggleButtonFacade;

public class ToggleButtonAction extends AbstractButtonAction implements ToggleButtonFacade {

	public ToggleButtonAction() {
		super(new JToggleButton());
	}

}
