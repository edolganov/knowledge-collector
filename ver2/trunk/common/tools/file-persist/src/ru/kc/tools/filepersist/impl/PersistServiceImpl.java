package ru.kc.tools.filepersist.impl;

import java.io.File;

import ru.kc.exception.BaseException;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.tools.filepersist.ServiceListener;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.Updater;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class PersistServiceImpl implements PersistService {
	
	InitParams params;
	Context context;
	
	public void init(InitParams params)throws Exception {
		this.params = params;
		createRootDir();
		File nodesDir = new File(params.rootDir,"nodes");
		File blobsDir = new File(params.rootDir,"nodes-data");
		
		InitContextExt init = new InitContextExt(params,nodesDir,blobsDir);
		FactoryImpl factory = new FactoryImpl();
		TreeImpl tree = new TreeImpl();		
		FileSystemImpl fs = new FileSystemImpl();
		UpdaterImpl updater = new UpdaterImpl();
		Listeners listeners = new Listeners();

		
		context = new Context(init, factory, tree, fs, updater, listeners);
		fs.init(context);
		tree.init(context);
		factory.init(context);
		updater.init(context);
		
	}
	
	private void createRootDir() throws BaseException {
		params.rootDir.mkdirs();
		if(!params.rootDir.isDirectory()){
			throw new BaseException("!root.isDirectory(): "+params.rootDir);
		}
	}
	
	@Override
	public void addListener(ServiceListener listener) {
		context.listeners.addListener(listener);
	}
	
	public Tree tree(){
		return context.tree;
	}
	
	public Factory factory(){
		return context.factory;
	}

	@Override
	public Updater updater() {
		return context.updater;
	}

}
