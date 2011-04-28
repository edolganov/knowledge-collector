package ru.kc.module.tree.tools.hotkeys;

import ru.kc.model.Link;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.os.OSUtil;

@Mapping(Tree.class)
public class OpenUrlController extends AbstractOpenController<Link, Tree> {
	
	@Override
	protected void init() {
		init(Link.class, ui.tree, true);
	}

	protected void open(Link link) {
		try{
			OSUtil.openUrl(link.getUrl());
		}catch (Exception e) {
			log.error("can't open url", e);
		}
	}

}
