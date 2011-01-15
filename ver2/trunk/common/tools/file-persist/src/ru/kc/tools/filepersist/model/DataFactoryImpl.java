package ru.kc.tools.filepersist.model;

import ru.kc.tools.filepersist.model.impl.DirBean;
import ru.kc.tools.filepersist.model.impl.NodeBean;

public class DataFactoryImpl {

	public NodeBean createDir(String name) {
		DirBean dir = new DirBean();
		init(dir,name);
		return dir;
	}

	private void init(NodeBean node, String name) {
		node.setId(generateId());
		node.setCreateDate(System.currentTimeMillis());
		node.setName(name);
	}

	private String generateId() {
		return new StringBuilder()
			.append(System.currentTimeMillis())
			.append('-')
			.append(System.nanoTime())
			.toString();
	}

}
