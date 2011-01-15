package ru.kc.tools.filepersist.impl;

public class Context {
	
	public final InitContextExt init;
	public final FactoryImpl factory;
	public final TreeImpl tree;
	
	public Context(InitContextExt init, FactoryImpl factory,
			TreeImpl tree) {
		super();
		this.init = init;
		this.factory = factory;
		this.tree = tree;
	}
	



}
