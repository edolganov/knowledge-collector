package ru.kc.tools.filepersist.impl;

import java.io.File;
import java.util.Collection;

import ru.kc.exception.BaseException;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.tools.filepersist.command.CreateOrLoadData;
import ru.kc.tools.filepersist.model.DataFactoryImpl;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class PersistServiceImpl implements PersistService {
	
	private Context c;
	
	public PersistServiceImpl() {
	}
	
	public void init(String rootDirPath, InitContext params) throws Exception{
		initContext(rootDirPath,params);
		c.invoke(new CreateOrLoadData());
	}

	private void initContext(String rootDirPath, InitContext params) throws Exception {
		File root = createRootDir(rootDirPath);
		File nodesDir = new File(root,"nodes");
		File blobsDir = new File(root,"nodes-data");
		InitContext init = new InitContext(nodesDir,blobsDir,params.maxNodesInContainer, params.maxContainerFilesInFolder, params.maxFoldersInLevel);
		
		FileSystemImpl fs = new FileSystemImpl();
		fs.init(root,this,init);
		
		DataFactoryImpl dataFactory = new DataFactoryImpl();
		
		c = new Context(fs,dataFactory,this,new ConvertorServiceImpl());
	}

	private File createRootDir(String rootDirPath) throws BaseException {
		File root = new File(rootDirPath);
		root.mkdirs();
		root.mkdir();
		if(!root.isDirectory()){
			throw new BaseException("!root.isDirectory() with path "+rootDirPath);
		}
		return root;
	}
	

	
	@Override
	public Node getRoot() throws Exception{
		return c.fs.getRoot();
	}

	@Override
	public Collection<Node> getChildren(Node node) {
		return c.fs.getChildren(convert(node));
	}
	
	@Override
	public void add(Node parent, Node node) throws Exception {
		c.fs.create(convert(parent), convert(parent));
	}
	
	
	

	private NodeBean convert(Node node) {
		if(node != null){
			if(node instanceof NodeBean) return (NodeBean) node;
			//else
			throw new IllegalArgumentException("unknow node type: "+node.getClass());
		} else {
			throw new IllegalArgumentException("node is null");
		}

	}

}
