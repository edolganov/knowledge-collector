package ru.kc.tools.filepersist.persist;

import java.io.File;
import java.io.IOException;

import ru.kc.exception.BaseException;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.ContainersTree;
import ru.kc.tools.filepersist.model.impl.NodeBean;

public class FileSystemImpl {
	
	private File nodesDir;
	private File refsDir;
	private File blobsDir;
	
	private ContainersTree containersTree = new ContainersTree();
	private ContainerNameModel containerNameModel = new ContainerNameModel();
	private ContainerStore containerStore = new ContainerStore();
	
	
	public void init(File rootDir) throws IOException{
		nodesDir = new File(rootDir,"nodes");
		refsDir = new File(rootDir,"refs");
		blobsDir = new File(rootDir,"nodes-data");
		
		initFolders();
		initRootNode();
	}



	private void initFolders() {
		if(!nodesDir.exists()) nodesDir.mkdir();
		if(!refsDir.exists()) refsDir.mkdir();
		if(!blobsDir.exists()) blobsDir.mkdir();
	}
	
	private void initRootNode() throws IOException {
		Container container = null;
		
		String name = containerNameModel.getFirstName();
		File file = new File(nodesDir,name);
		if(file.exists()){
			container = containerStore.load(file);
		} else {
			container = Container.create(file);
			containerStore.save(container);
		}
		
		containersTree.setRoot(container);
	}
	
	public NodeBean loadRoot(){
		Container c = containersTree.getRoot();
		if(c.size() > 0){
			return c.get(0);
		} else {
			return null;
		}
	}

	public void create(NodeBean node) throws Exception {
		NodeBean parent = node.getParent();
		if(parent != null){
			//TODO
		} else {
			Container c = containersTree.getRoot();
			if(c.size() > 0) throw new BaseException("node already exist");
			
			c.add(node);
			containerStore.save(c);
		}
	}

	
	


}
