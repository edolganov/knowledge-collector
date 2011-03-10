package ru.kc.module.tree.tools;

import ru.kc.model.FileLink;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.os.OSUtil;

@Mapping(Tree.class)
public class OpenFileLinkController extends AbstractOpenController<FileLink, Tree> {
	
	@Override
	protected void init() {
		init(FileLink.class, ui.tree, true);
	}

	protected void open(FileLink file) {
		try{
			OSUtil.openFile(file.getPath());
		}catch (Exception e) {
			log.error("can't open url", e);
		}
	}

}
