package ru.kc.main.node;

import ru.kc.main.common.Controller;
import ru.kc.main.node.ui.dialog.NodeDialog;
import ru.kc.model.Dir;
import ru.kc.platform.annotations.Mapping;

@Mapping(NodeDialog.class)
public class CreateDirController extends Controller<NodeDialog>{

	@Override
	protected void init() {
		ui.rootPanel.removeAll();
	}

	public Dir getDir() {
		return persistFactory.createDir("test-"+System.currentTimeMillis(), null);
	}

}
