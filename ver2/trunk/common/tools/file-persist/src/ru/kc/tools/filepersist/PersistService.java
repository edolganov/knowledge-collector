package ru.kc.tools.filepersist;

import java.io.File;

import ru.kc.exception.BaseException;
import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.impl.FactoryImpl;
import ru.kc.tools.filepersist.impl.InitContextExt;
import ru.kc.tools.filepersist.impl.TreeImpl;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class PersistService {
	
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

		
		context = new Context(init, factory, tree, fs);
		fs.init(context);
		tree.init(context);
		factory.init(context);
		
	}
	
	private void createRootDir() throws BaseException {
		params.rootDir.mkdirs();
		if(!params.rootDir.isDirectory()){
			throw new BaseException("!root.isDirectory(): "+params.rootDir);
		}
	}
	
	public Tree tree(){
		return context.tree;
	}
	
	public Factory factory(){
		return context.factory;
	}

}
