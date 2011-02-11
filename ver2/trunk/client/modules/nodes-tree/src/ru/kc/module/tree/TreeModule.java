package ru.kc.module.tree;

import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.GlobalMapping;
import ru.kc.platform.module.Module;

@GlobalMapping("nodes-tree")
public class TreeModule extends Module<Tree>{

	@Override
	protected Tree createUI() {
		return new Tree();
	}

}
