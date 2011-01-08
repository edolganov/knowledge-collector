package ru.kc.tools.filepersist.persist;

import java.io.File;

import ru.kc.tools.filepersist.model.impl.NodeBean;

public class EntityManager {
	
	private FileSystemImpl fs = new FileSystemImpl();


	public void init(File rootDir) throws Exception {
		fs.init(rootDir);
	}

	public NodeBean getRoot() throws Exception {
		return fs.loadRoot();
	}


	public void create(NodeBean node) throws Exception {
		fs.create(node);
	}



}
