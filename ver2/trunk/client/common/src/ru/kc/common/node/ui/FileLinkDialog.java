package ru.kc.common.node.ui;

import java.awt.Dimension;
import java.awt.Frame;

import ru.kc.platform.ui.dialog.OkCancelDialog;

@SuppressWarnings("serial")
public class FileLinkDialog extends OkCancelDialog{

	public FilePanel panel = new FilePanel();
	
	public FileLinkDialog(Frame parent, boolean modal) {
		super(parent, modal);
		rootPanel.add(panel);
		
		Dimension oldSize = getSize();
		int width = (int) oldSize.getWidth() + 30;
		int height = (int) oldSize.getHeight() + 30;
		setSize(width, height);
	}

}
