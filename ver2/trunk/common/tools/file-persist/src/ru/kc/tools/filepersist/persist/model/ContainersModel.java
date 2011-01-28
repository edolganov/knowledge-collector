package ru.kc.tools.filepersist.persist.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.ContainersFolder;
import ru.kc.tools.filepersist.persist.FSContext;
import ru.kc.util.collection.LimitedLevelTreeList;
import ru.kc.util.collection.LimitedLevelTreeList.TreeNode;

public class ContainersModel {
	
	private static final String CONTAINER_FILE_EXT = ".xml";
	
	private FSContext c;
	private NameModel nameModel;
	
	//data
	private LimitedLevelTreeList<ContainersFolder> folderTreeList;
	private HashMap<String, Container> containersCache = new HashMap<String, Container>();
	private File firstFile;
	
	public void init(FSContext c){
		this.c = c;
		folderTreeList = new LimitedLevelTreeList<ContainersFolder>(c.c.init.params.maxFoldersInLevel);
		nameModel = new NameModel();
		String name = nameModel.first(CONTAINER_FILE_EXT);
		firstFile = new File(c.c.init.nodesDir,name);
	}
	
	public Container createRootContainer() {
		return Container.create(firstFile,c.c);
	}
	
	public void setRoot(Container container){
		if(folderTreeList.size() > 0) throw new IllegalStateException("root is already set");
		if(!firstFile.equals(container.getFile()))throw new IllegalStateException("container file "+container.getFile()+" is unknow: "+firstFile+" expected");
			
		ContainersFolder rootFolder = new ContainersFolder(c.c.init.nodesDir, c.c.init.params.maxContainerFilesInFolder);
		rootFolder.add(container);
		
		folderTreeList.setRoot(rootFolder);
		containersCache.put(container.getFileSimplePath(), container);
	}

	public Container getRoot() {
		ContainersFolder folder = folderTreeList.getRoot();
		if(!folder.isEmpty()) {
			Container container = folder.get(0);
			return container;
		} else {
			throw new IllegalStateException("no root in model");
		}
	}
	
	public Container createNextContainer(){
		ContainersFolder lastFolder = folderTreeList.get(folderTreeList.size()-1);
		if(!lastFolder.isFull()){
			File lastExistFile = lastFolder.getLast().getFile();
			String newName = nameModel.next(lastExistFile.getName());
			File newFile = new File(lastFolder.file,newName);
			Container container = Container.create(newFile, c.c);
			return container;
		} else {
			TreeNode<ContainersFolder> node = folderTreeList.getParentCandidat();
			List<TreeNode<ContainersFolder>> children = node.getChildren();
			File parentFolderFile = node.getOb().file;
			File newFolderFile = null;
			if(children.size() == 0){
				newFolderFile = new File(parentFolderFile,nameModel.first());
			} else {
				TreeNode<ContainersFolder> lastChild = children.get(children.size()-1);
				String lastChildFolderName = lastChild.getOb().file.getName();
				newFolderFile = new File(parentFolderFile,nameModel.next(lastChildFolderName));
			}				
			File newContainerFile = new File(newFolderFile,nameModel.first(CONTAINER_FILE_EXT));
			Container container = Container.create(newContainerFile, c.c);
			return container;
		}
	}
	
	public void add(Container container) {
		File file = container.getFile();
		if(file == null) throw new IllegalArgumentException("file is null in "+container);
		
		ContainersFolder parentFolder = null;
		ContainersFolder lastFolder = folderTreeList.get(folderTreeList.size()-1);
		if(!lastFolder.isFull()){
			parentFolder = lastFolder;
		} else {
			TreeNode<ContainersFolder> node = folderTreeList.getParentCandidat();
			List<TreeNode<ContainersFolder>> children = node.getChildren();
			File parentFolderFile = node.getOb().file;
			File newFolderFile = null;
			if(children.size() == 0){
				newFolderFile = new File(parentFolderFile,nameModel.first());
			} else {
				TreeNode<ContainersFolder> lastChild = children.get(children.size()-1);
				String lastChildFolderName = lastChild.getOb().file.getName();
				newFolderFile = new File(parentFolderFile,nameModel.next(lastChildFolderName));
			}				
			
			ContainersFolder newFolder = new ContainersFolder(newFolderFile, c.c.init.params.maxContainerFilesInFolder);
			folderTreeList.add(newFolder);
			containersCache.put(container.getFileSimplePath(), container);
			parentFolder = newFolder;
		}
		
		File nextContainerFile = null;
		if(parentFolder.size() == 0){
			nextContainerFile = new File(parentFolder.file,nameModel.first(CONTAINER_FILE_EXT));
		} else {
			Container lastContainer = parentFolder.getLast();
			File lastFileInFolder = lastContainer.getFile();
			String name = lastFileInFolder.getName();
			nextContainerFile = new File(parentFolder.file,nameModel.next(name));
		}
		
		if(!file.equals(nextContainerFile)) throw new IllegalArgumentException("invalid file "+file+" of "+container);
	}
	
	public Container getNotFullContainer(){
		//ищем не полный контейнер среди существующих
		Container firstNotFullContainer = null;
		for(ContainersFolder folder : folderTreeList){
			for (Container container : folder) {
				if(!container.isFull()){
					firstNotFullContainer = container;
					break;
				}
			}
		}
		if(firstNotFullContainer != null) {
			return firstNotFullContainer;
		}
		else {
			//создаем новый пустой контейнер
			ContainersFolder lastFolder = folderTreeList.get(folderTreeList.size()-1);
			if(!lastFolder.isFull()){
				File lastExistFile = lastFolder.getLast().getFile();
				String newName = nameModel.next(lastExistFile.getName());
				File newFile = new File(lastFolder.file,newName);
				Container container = Container.create(newFile, c.c);
				lastFolder.add(container);
				containersCache.put(container.getFileSimplePath(), container);
				return container;
				
			} else {
				//создаем новую папку и первый контейнер
				TreeNode<ContainersFolder> node = folderTreeList.getParentCandidat();
				List<TreeNode<ContainersFolder>> children = node.getChildren();
				File parentFolderFile = node.getOb().file;
				File newFolderFile = null;
				if(children.size() == 0){
					newFolderFile = new File(parentFolderFile,nameModel.first());
				} else {
					TreeNode<ContainersFolder> lastChild = children.get(children.size()-1);
					String lastChildFolderName = lastChild.getOb().file.getName();
					newFolderFile = new File(parentFolderFile,nameModel.next(lastChildFolderName));
				}				
				File newContainerFile = new File(newFolderFile,nameModel.first(CONTAINER_FILE_EXT));
				Container container = Container.create(newContainerFile, c.c);
				
				ContainersFolder newFolder = new ContainersFolder(newFolderFile, c.c.init.params.maxContainerFilesInFolder);
				newFolder.add(container);
				
				folderTreeList.add(newFolder);
				containersCache.put(container.getFileSimplePath(), container);
				return container;
			}
		}
	}

	public Container getContainer(String fileSimplePath) {
		return containersCache.get(fileSimplePath);
	}




}
