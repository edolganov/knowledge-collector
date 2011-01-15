package ru.kc.tools.filepersist.impl;

import ru.kc.model.Dir;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.model.impl.DirBean;
import ru.kc.tools.filepersist.model.impl.NodeBean;

public class FactoryImpl implements Factory {

	public Dir createDir(String name) {
		DirBean dir = new DirBean();
		init(dir,name);
		return dir;
	}

	@Override
	public FileLink createFileLink(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Link createLink(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Text createText(String name) {
		// TODO Auto-generated method stub
		return null;
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
