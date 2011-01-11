package ru.kc.tools.filepersist;

import java.io.File;
import java.util.Collection;

import ru.kc.exception.BaseException;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.command.Command;
import ru.kc.tools.filepersist.command.CreateOrLoadData;
import ru.kc.tools.filepersist.model.DataFactory;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class PersistService {
	
	private Context c;
	
	public PersistService() {
	}
	
	public void init(String rootDirPath) throws Exception{
		initContext(rootDirPath);
		invoke(new CreateOrLoadData());
	}

	private void initContext(String rootDirPath) throws Exception {
		File root = createRootDir(rootDirPath);
		
		FileSystemImpl fs = new FileSystemImpl();
		fs.init(root,this);
		
		DataFactory dataFactory = new DataFactory();
		
		c = new Context(root,
				fs,
				dataFactory,
				this);
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
	
	private <O> O invoke(Command<O> command) throws Exception{
		return c.invoke(command);
	}
	
	
	public Node getRoot() throws Exception{
		return c.fs.getRoot();
	}

	public Collection<Node> getChildren(Node node) {
		return c.fs.getChildren(convert(node));
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
