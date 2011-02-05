package ru.kc.main.tree;

import ru.kc.main.tree.ui.Tree;
import ru.kc.platform.module.Module;

public class TreeModule extends Module<Tree>{

	@Override
	protected Tree createUI() {
		return new Tree();
	}

}
