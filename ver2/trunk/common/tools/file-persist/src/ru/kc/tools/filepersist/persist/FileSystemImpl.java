package ru.kc.tools.filepersist.persist;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import ru.kc.exception.BaseException;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.model.ContainersModel;
import ru.kc.tools.filepersist.persist.transaction.Transaction;
import ru.kc.tools.filepersist.persist.transaction.actions.AddChild;
import ru.kc.tools.filepersist.persist.transaction.actions.AddNodeToContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.GetChildren;
import ru.kc.tools.filepersist.persist.transaction.actions.GetNotFullContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.GetParent;
import ru.kc.tools.filepersist.persist.transaction.actions.RemoveChild;
import ru.kc.tools.filepersist.persist.transaction.actions.RemoveNodeFromContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.ReplaceNodeInContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.SaveContainer;
import ru.kc.tools.filepersist.persist.transaction.actions.SaveContainers;

public class FileSystemImpl {
	
	private FSContext c;
	
	public void init(Context context) throws IOException{
		ContainerStore containerStore = new ContainerStore();
		ContainersModel containerModel = new ContainersModel();
		
		c = new FSContext(
				containerModel, 
				containerStore,
				context);
		containerStore.init(c);
		containerModel.init(c);
		
		initFolders();
		initRootContainer();
		loadAllContainers();
	}

	private void initFolders() {
		if(!c.c.init.nodesDir.exists()) c.c.init.nodesDir.mkdir();
		if(!c.c.init.blobsDir.exists()) c.c.init.blobsDir.mkdir();
	}
	
	private void initRootContainer() throws IOException {		
		Container container = c.containerModel.createRootContainer();
		File file = container.getFile();
		if(file.exists()){
			container = c.containerStore.load(file);
		} else {
			c.containerStore.save(container);
		}
		
		c.containerModel.setRoot(container);
	}
	

	private void loadAllContainers() throws IOException {
		Container next = c.containerModel.createNextContainer();
		while(true){
			File file = next.getFile();
			if(file.exists()){
				Container loaded = c.containerStore.load(file);
				c.containerModel.add(loaded);
				next = c.containerModel.createNextContainer();
			} else {
				break;
			}
		}
		
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
				invoke(new AddNodeToContainer(node,containerForChild));
				invoke(new AddChild(parent, node));
				invoke(new SaveContainers(parent, node));
				
				return null;
			}
			
		}.start();
	}
	
	public List<NodeBean> getChildren(final NodeBean node) throws Exception {
		return new Transaction<List<NodeBean>>(c) {

			@Override
			protected List<NodeBean> body() throws Throwable {
				return invoke(new GetChildren(node));
			}
		}.start();
	}
	
	public Node getParent(final NodeBean node) throws Exception {
		return new Transaction<NodeBean>(c) {

			@Override
			protected NodeBean body() throws Throwable {
				return invoke(new GetParent(node));
			}
		}.start();
	}

	public void replace(final NodeBean old, final NodeBean newNode)throws Exception {
		new Transaction<Void>(c) {

			@Override
			protected Void body() throws Throwable {
				invoke(new ReplaceNodeInContainer(old, newNode));
				Container container = newNode.getContainer();
				invoke(new SaveContainer(container));
				return null;
			}
		}.start();
	}
	
	public void deleteRecursive(final NodeBean node)throws Exception{
		new Transaction<Void>(c) {

			@Override
			protected Void body() throws Throwable {
				//собираем список нод на удаление
				LinkedList<NodeBean> toDeleteQueue = new LinkedList<NodeBean>();
				LinkedList<NodeBean> nodesQueue = new LinkedList<NodeBean>();
				nodesQueue.addLast(node);
				while(nodesQueue.size() > 0){
					NodeBean curNode = nodesQueue.removeFirst();
					toDeleteQueue.addFirst(curNode);
					
					List<NodeBean> children = invoke(new GetChildren(curNode));
					for(NodeBean child : children){
						nodesQueue.addLast(child);
					}
				}
				
				//удаляем ноды, собирая список контейнеров на сохранение
				HashSet<Container> containersToSave = new HashSet<Container>();
				while(toDeleteQueue.size() > 0){
					NodeBean toDelete = toDeleteQueue.removeFirst();
					NodeBean parent = invoke(new GetParent(toDelete));
					Container container = toDelete.getContainer();
					
					invoke(new RemoveChild(parent, toDelete));
					invoke(new RemoveNodeFromContainer(toDelete));
					containersToSave.add(container);
				}
				
				//сохраняем контейнеры
				for (Container container : containersToSave) {
					invoke(new SaveContainer(container));
				}
				
				return null;
			}
			
		}.start();
	}
	

	
	public void move(NodeBean node, NodeBean newParent)throws Exception{
		//TODO
	}






}
