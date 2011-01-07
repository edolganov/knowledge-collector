package ru.kc.tools.filepersist.model;

import ru.kc.tools.filepersist.model.impl.DirBean;
import ru.kc.tools.filepersist.model.impl.NodeBean;

public class DataFactory {

	public NodeBean createDir(NodeBean parent,String name) {
		DirBean dir = new DirBean();
		init(dir,parent,name);
		return dir;
	}

	private void init(NodeBean node, NodeBean parent,String name) {
		node.setId(generateId());
		node.setCreateDate(System.currentTimeMillis());
		node.setName(name);
		node.setParent(parent);
	}

	private String generateId() {
		return new StringBuilder()
			.append(System.currentTimeMillis())
			.append('-')
			.append(System.nanoTime())
			.toString();
	}

}
