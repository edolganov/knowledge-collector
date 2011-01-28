package ru.kc.tools.filepersist.persist.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.Folder;
import ru.kc.tools.filepersist.persist.FSContext;
import ru.kc.util.collection.LimitedLevelTreeList;
import ru.kc.util.collection.LimitedLevelTreeList.TreeNode;

public class ContainersModel {
	
	private static final String CONTAINER_FILE_EXT = ".xml";
	
	private FSContext c;
	private NameModel nameModel;
	
	//data
	private LimitedLevelTreeList<Folder> folderTreeList;
	private HashMap<String, Container> containersCache = new HashMap<String, Container>();
	private File firstFile;
	
	public void init(FSContext c){
		this.c = c;
		folderTreeList = new LimitedLevelTreeList<Folder>(c.c.init.params.maxFoldersInLevel);
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
			
		Folder rootFolder = new Folder(c.c.init.nodesDir, c.c.init.params.maxContainerFilesInFolder);
		rootFolder.add(container);
		
		folderTreeList.setRoot(rootFolder);
		containersCache.put(container.getFileSimplePath(), container);
	}

	public Container getRoot() {
		Folder folder = folderTreeList.getRoot();
		if(!folder.isEmpty()) {
			Container container = folder.get(0);
			return container;
		} else {
			throw new IllegalStateException("no root in model");
		}
	}
	
	public Container createNextContainer(){
		Folder lastFolder = folderTreeList.get(folderTreeList.size()-1);
		if(!lastFolder.isFull()){
			File lastExistFile = lastFolder.getLast().getFile();
			String newName = nameModel.next(lastExistFile.getName());
			File newFile = new File(lastFolder.file,newName);
			Container container = Container.create(newFile, c.c);
			return container;
		} else {
			TreeNode<Folder> node = folderTreeList.getParentCandidat();
			List<TreeNode<Folder>> children = node.getChildren();
			File parentFolderFile = node.getOb().file;
			File newFolderFile = null;
			if(children.size() == 0){
				newFolderFile = new File(parentFolderFile,nameModel.first());
			} else {
				TreeNode<Folder> lastChild = children.get(children.size()-1);
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
		
		Folder parentFolder = null;
		Folder lastFolder = folderTreeList.get(folderTreeList.size()-1);
		if(!lastFolder.isFull()){
			parentFolder = lastFolder;
		} else {
			TreeNode<Folder> node = folderTreeList.getParentCandidat();
			List<TreeNode<Folder>> children = node.getChildren();
			File parentFolderFile = node.getOb().file;
			File newFolderFile = null;
			if(children.size() == 0){
				newFolderFile = new File(parentFolderFile,nameModel.first());
			} else {
				TreeNode<Folder> lastChild = children.get(children.size()-1);
				String lastChildFolderName = lastChild.getOb().file.getName();
				newFolderFile = new File(parentFolderFile,nameModel.next(lastChildFolderName));
			}				
			
			Folder newFolder = new Folder(newFolderFile, c.c.init.params.maxContainerFilesInFolder);
			folderTreeList.add(newFolder);
			containersCache.put(container.getFileSimplePath(), container);
			parentFolder = newFolder;
		}
		
		assertEqualsActualAndExceptedFiles(container,parentFolder);
		
		parentFolder.add(container);
		containersCache.put(container.getFileSimplePath(), container);
		
	}
	
	private void assertEqualsActualAndExceptedFiles(Container container, Folder folder) {
		File expected = null;
		if(folder.size() == 0){
			expected = new File(folder.file,nameModel.first(CONTAINER_FILE_EXT));
		} else {
			Container lastContainer = folder.getLast();
			File lastFileInFolder = lastContainer.getFile();
			String name = lastFileInFolder.getName();
			expected = new File(folder.file,nameModel.next(name));
		}
		
		File actual = container.getFile();
		if(!actual.equals(expected)) 
			throw new IllegalArgumentException("invalid file "+actual+" of "+container);
	}
	

	public Container getNotFullContainer(){
		//ищем не полный контейнер среди существующих
		Container firstNotFullContainer = null;
		for(Folder folder : folderTreeList){
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
			Folder lastFolder = folderTreeList.get(folderTreeList.size()-1);
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
				TreeNode<Folder> node = folderTreeList.getParentCandidat();
				List<TreeNode<Folder>> children = node.getChildren();
				File parentFolderFile = node.getOb().file;
				File newFolderFile = null;
				if(children.size() == 0){
					newFolderFile = new File(parentFolderFile,nameModel.first());
				} else {
					TreeNode<Folder> lastChild = children.get(children.size()-1);
					String lastChildFolderName = lastChild.getOb().file.getName();
					newFolderFile = new File(parentFolderFile,nameModel.next(lastChildFolderName));
				}				
				File newContainerFile = new File(newFolderFile,nameModel.first(CONTAINER_FILE_EXT));
				Container container = Container.create(newContainerFile, c.c);
				
				Folder newFolder = new Folder(newFolderFile, c.c.init.params.maxContainerFilesInFolder);
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
