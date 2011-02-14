package ru.kc.tools.filepersist.impl;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.Updater;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class UpdaterImpl implements Updater {
	
	TreeImpl tree;
	FileSystemImpl fs;
	
	public void init(Context context){
		tree = context.tree;
		fs = context.fs;
	}

	@Override
	public void updateNode(Node node, String name) {
		
	}

	@Override
	public void updateNode(Node node, String name, String description) {
		// TODO Auto-generated method stub

	}

}
