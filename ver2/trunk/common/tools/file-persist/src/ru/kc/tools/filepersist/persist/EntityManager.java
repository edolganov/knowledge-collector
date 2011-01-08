package ru.kc.tools.filepersist.persist;

import java.io.File;
import java.io.IOException;

import ru.kc.tools.filepersist.model.impl.NodeBean;

public class EntityManager {
	
	private FileSystemImpl fs = new FileSystemImpl();


	public void init(File rootDir) throws IOException {
		fs.init(rootDir);
	}

	public NodeBean getRoot() throws IOException {
		return fs.loadRoot();
	}


	public void save(NodeBean node) throws IOException {
		fs.save(node);
	}



}
