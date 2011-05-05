package ru.kc.module.tree;

import ru.kc.common.FocusProvider;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.GlobalMapping;
import ru.kc.platform.module.Module;

@GlobalMapping("nodes-tree")
public class TreeModule extends Module<Tree> implements FocusProvider {

	@Override
	protected Tree createUI() {
		return new Tree();
	}

	@Override
	public void setFocusRequest() {
		getController(TreeController.class).setFocusRequest();
	}

}
