package ru.kc.tools.filepersist.persist;

import java.io.File;
import java.io.IOException;

import ru.kc.exception.BaseException;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.model.ContainerNameModel;
import ru.kc.tools.filepersist.persist.model.ContainersModel;

public class FileSystemImpl {
	
	private PersistService persistService;
	private File nodesDir;
	private File refsDir;
	private File blobsDir;
	
	private ContainersModel containerModel = new ContainersModel();
	private ContainerNameModel containerNameModel = new ContainerNameModel();
	private ContainerStore containerStore;
	
	
	public void init(File rootDir, PersistService persistService) throws IOException{
		nodesDir = new File(rootDir,"nodes");
		refsDir = new File(rootDir,"refs");
		blobsDir = new File(rootDir,"nodes-data");
		this.persistService = persistService;
		containerStore = new ContainerStore(persistService);
		
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
			container = Container.create(file,persistService);
			containerStore.save(container);
		}
		
		containerModel.setRoot(container);
	}
	
	
	
	
	public NodeBean getRoot(){
		Container c = containerModel.getRoot();
		return c.getFirst();
	}
	
	public void createRoot(NodeBean node)throws Exception{
		Container c = containerModel.getRoot();
		if(c.size() > 0) throw new BaseException("root already exist");
		
		c.add(node);
		containerStore.save(c);
	}

	public void create(NodeBean parent, NodeBean node) throws Exception {
		//TODO
	}
	
	public void deleteRecursive(NodeBean node)throws Exception{
		//TODO
	}
	
	public void update(NodeBean node)throws Exception {
		//TODO
	}
	
	public void moveRecursive(NodeBean node, NodeBean newParent)throws Exception{
		//TODO
	}
	


	


}
