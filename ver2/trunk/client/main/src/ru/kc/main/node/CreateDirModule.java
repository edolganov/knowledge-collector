package ru.kc.main.node;

import java.awt.Frame;

import ru.kc.platform.ui.dialog.OkCancelDialog;
import ru.kc.main.node.ui.dialog.DirDialog;
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
