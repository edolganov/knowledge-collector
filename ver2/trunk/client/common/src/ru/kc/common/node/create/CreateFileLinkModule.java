package ru.kc.common.node.create;

import java.awt.Frame;

import ru.kc.common.node.ui.FileLinkDialog;
import ru.kc.model.FileLink;
import ru.kc.platform.module.DialogModule;

public class CreateFileLinkModule extends DialogModule<FileLinkDialog>{

	@Override
	protected FileLinkDialog createUI(Frame parent, boolean modal) {
		return new FileLinkDialog(parent, modal);
	}

	public FileLink getNode() {
		return getController(CreateFileLinkController.class).getNode();
	}
	
	

}
