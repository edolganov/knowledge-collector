package ru.kc.common.node.create;

import java.awt.Frame;

import ru.kc.common.node.ui.TextDialog;
import ru.kc.model.Text;
import ru.kc.platform.module.DialogModule;

public class CreateTextModule extends DialogModule<TextDialog>{

	@Override
	protected TextDialog createUI(Frame parent, boolean modal) {
		return new TextDialog(parent, modal);
	}

	public Text getText() {
		return getController(CreateTextController.class).getValue();
	}
	
	

}
