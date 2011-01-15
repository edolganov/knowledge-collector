package ru.kc.tools.filepersist.persist;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import ru.kc.exception.BaseException;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.impl.InitContext;
import ru.kc.tools.filepersist.impl.TreeImpl;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.model.ContainersModel;
import ru.kc.tools.filepersist.persist.transaction.Transaction;
import ru.kc.tools.filepersist.persist.transaction.actions.AddChild;
import ru.kc.tools.filepersist.persist.transaction.actions.AddNodeToContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.GetNotFullContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.SaveContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.SaveContainers;

public class FileSystemImpl {
	
	private FSContext c;
	
	public void init(File rootDir, 
			TreeImpl persistService, InitContext init) throws IOException{
		ContainerStore containerStore = new ContainerStore();
		ContainersModel containerModel = new ContainersModel();
		
		c = new FSContext(
				containerModel, 
				containerStore,
				persistService,
				init);
		containerStore.init(c);
		containerModel.init(c);
		
		initFolders();
		initRootNode();
	}

	private void initFolders() {
		if(!c.init.nodesDir.exists()) c.init.nodesDir.mkdir();
		if(!c.init.blobsDir.exists()) c.init.blobsDir.mkdir();
	}
	
	private void initRootNode() throws IOException {		
		Container container = c.containerModel.createRootContainer();
		File file = container.getFile();
		if(file.exists()){
			container = c.containerStore.load(file);
		} else {
			c.containerStore.save(container);
		}
		
		c.containerModel.setRoot(container);
	}
	
	
	
	
	public NodeBean getRoot(){
		Container container = c.containerModel.getRoot();
		return container.getFirst();
	}
	
	public void createRoot(final NodeBean node)throws Exception{
		new Transaction<Void>(c){

			@Override
			protected Void body() throws Throwable {
				Container container = c.containerModel.getRoot();
				if(container.size() > 0) throw new BaseException("root already exist");
				
				invoke(new AddNodeToContainer(node,container));
				invoke(new SaveContainer(container));
				
				return null;
			}
			
		}.start();
	}

	public void create(final NodeBean parent, final NodeBean node) throws Exception {				
		new Transaction<Void>(c){

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


}
