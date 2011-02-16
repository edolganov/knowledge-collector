package ru.kc.common.node.create;

import java.awt.Frame;

import ru.kc.common.node.ui.LinkDialog;
import ru.kc.model.Link;
import ru.kc.platform.module.DialogModule;

public class CreateLinkModule extends DialogModule<LinkDialog>{

	@Override
	protected LinkDialog createUI(Frame parent, boolean modal) {
		return new LinkDialog(parent, modal);
	}

	public Link getLink() {
		return null;//getController(CreateDirController.class).getDir();
	}
	
	

}
