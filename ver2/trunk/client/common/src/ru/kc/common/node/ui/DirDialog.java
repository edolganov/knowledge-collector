package ru.kc.common.node.ui;

import java.awt.Frame;

import ru.kc.platform.ui.dialog.OkCancelDialog;

@SuppressWarnings("serial")
public class DirDialog extends OkCancelDialog{

	public NodePanel panel = new NodePanel();
	
	public DirDialog(Frame parent, boolean modal) {
		super(parent, modal);
		rootPanel.add(panel);
	}

}
