package ru.kc.tools.filepersist.impl;

import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class Context {
	
	public final InitContextExt init;
	public final FactoryImpl factory;
	public final TreeImpl tree;
	public final FileSystemImpl fs;
	
	public Context(InitContextExt init, FactoryImpl factory, TreeImpl tree,
			FileSystemImpl fs) {
		super();
		this.init = init;
		this.factory = factory;
		this.tree = tree;
		this.fs = fs;
	}
	

	



}
