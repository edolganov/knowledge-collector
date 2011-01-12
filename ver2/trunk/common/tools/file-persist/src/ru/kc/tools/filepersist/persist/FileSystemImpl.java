package ru.kc.tools.filepersist.persist;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import ru.kc.exception.BaseException;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.model.ContainerNameModel;
import ru.kc.tools.filepersist.persist.model.ContainersModel;
import ru.kc.tools.filepersist.persist.transaction.Transaction;
import ru.kc.tools.filepersist.persist.transaction.actions.AddChild;
import ru.kc.tools.filepersist.persist.transaction.actions.AddNodeToContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.GetNotFullContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.SaveContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.SaveContainers;

public class FileSystemImpl {
	
	private PersistService persistService;
	private File nodesDir;
	private File blobsDir;
	
	private ContainersModel containerModel = new ContainersModel();
	private ContainerNameModel containerNameModel = new ContainerNameModel();
	private ContainerStore containerStore;
	
	
	public void init(File rootDir, PersistService persistService) throws IOException{
		nodesDir = new File(rootDir,"nodes");
		blobsDir = new File(rootDir,"nodes-data");
		this.persistService = persistService;
		containerStore = new ContainerStore(persistService);
		
		initFolders();
		initRootNode();
	}

	private void initFolders() {
		if(!nodesDir.exists()) nodesDir.mkdir();
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
	
	public void createRoot(final NodeBean node)throws Exception{
		new Transaction<Void>(createContext()){

			@Override
			protected Void body() throws Throwable {
				Container c = containerModel.getRoot();
				if(c.size() > 0) throw new BaseException("root already exist");
				
				invoke(new AddNodeToContainer(node,c));
				invoke(new SaveContainer(c));
				
				return null;
			}
			
		}.start();
	}

	public void create(final NodeBean parent, final NodeBean node) throws Exception {				
		new Transaction<Void>(createContext()){

			@Override
			protected Void body() throws Throwable {
				Container containerForChild = invoke(new GetNotFullContainer());
				invoke(new AddChild(parent, node));
				invoke(new AddNodeToContainer(node,containerForChild));
				invoke(new SaveContainers(parent, node));
				
				return null;
			}
			
		}.start();
	}

	public void update(NodeBean node)throws Exception {
		
	}
	
	public void deleteRecursive(NodeBean node)throws Exception{
		//TODO
	}
	

	
	public void moveRecursive(NodeBean node, NodeBean newParent)throws Exception{
		//TODO
	}

	public Collection<Node> getChildren(NodeBean node) {
		return null;
	}

	private Transaction.Context createContext(){
		return new Transaction.Context(containerModel, containerNameModel, containerStore);
	}


}
