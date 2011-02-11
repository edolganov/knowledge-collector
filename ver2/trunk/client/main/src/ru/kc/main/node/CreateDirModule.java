package ru.kc.main.node;

import java.awt.Frame;

import ru.kc.main.node.ui.dialog.NodeDialog;
import ru.kc.model.Dir;
import ru.kc.platform.module.DialogModule;

public class CreateDirModule extends DialogModule<NodeDialog>{

	@Override
	protected NodeDialog createUI(Frame parent, boolean modal) {
		return new NodeDialog(parent, modal);
	}

	public Dir getDir() {
		return getController(CreateDirController.class).getDir();
	}
	
	

}
