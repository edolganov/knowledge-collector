package ru.kc.main.node.ui.dialog;

import java.awt.Frame;

import ru.kc.platform.ui.dialog.OkCancelDialog;

public class DirDialog extends OkCancelDialog{

	public NodePanel panel = new NodePanel();
	
	public DirDialog(Frame parent, boolean modal) {
		super(parent, modal);
		rootPanel.add(panel);
	}

}
