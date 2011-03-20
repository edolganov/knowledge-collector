package ru.kc.components.dialog.ui;

import java.awt.Frame;

import ru.kc.platform.ui.dialog.OkCancelDialog;

public class OneTextFieldDialog extends OkCancelDialog{
	
	public OneTextFieldPanel panel = new OneTextFieldPanel();
	
	public OneTextFieldDialog(Frame parent, boolean modal) {
		super(parent, modal);
		rootPanel.add(panel);
	}

}
