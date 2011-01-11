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
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;
import ru.kc.tools.filepersist.persist.transaction.Transaction;
import ru.kc.tools.filepersist.persist.transaction.actions.AddChild;
import ru.kc.tools.filepersist.persist.transaction.actions.AddNodeToNotContainer;
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
	
	public void createRoot(NodeBean node)throws Exception{
		Container c = containerModel.getRoot();
		if(c.size() > 0) throw new BaseException("root already exist");
		
		doTransaction(
				new AddNodeToNotContainer(node,c),
				new SaveContainer(c));
	}

	public void create(NodeBean parent, NodeBean node) throws Exception {		
		Container containerForChild = containerModel.getNotFullContainer();
		
		doTransaction(
				new AddChild(parent, node),
				new AddNodeToNotContainer(node,containerForChild),
				new SaveContainers(parent, node));
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

	public Collection<Node> getChildren(NodeBean node) {
		return null;
	}
	

	
	private void doTransaction(AtomicAction<?>... actions) throws Exception {
		Transaction t = createTransaction();
		try {
			for (AtomicAction<?> atomicAction : actions) {
				t.invoke(atomicAction);
			}
		}catch (Exception e) {
			t.roolback();
			throw e;
		}
	}
	
	private Transaction createTransaction(){
		return new Transaction(containerModel, containerNameModel, containerStore);
	}


}
