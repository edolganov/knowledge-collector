package ru.kc.module.tree.tools;

import ru.kc.common.node.event.OpenNodeRequest;
import ru.kc.model.Text;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;

@Mapping(Tree.class)
public class OpenTextController extends AbstractOpenController<Text, Tree> {
	
	@Override
	protected void init() {
		init(Text.class, ui.tree, true);
	}

	protected void open(Text node) {
		fireEventInEDT(new OpenNodeRequest(node));
	}

}
