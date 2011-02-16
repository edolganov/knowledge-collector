package ru.kc.common.node.create;

import java.awt.Frame;

import ru.kc.common.node.ui.DirDialog;
import ru.kc.model.Dir;
import ru.kc.platform.module.DialogModule;

public class CreateDirModule extends DialogModule<DirDialog>{

	@Override
	protected DirDialog createUI(Frame parent, boolean modal) {
		return new DirDialog(parent, modal);
	}

	public Dir getDir() {
		return getController(CreateDirController.class).getDir();
	}
	
	

}
