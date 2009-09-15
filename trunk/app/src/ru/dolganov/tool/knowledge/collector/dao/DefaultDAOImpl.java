package ru.dolganov.tool.knowledge.collector.dao;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.chapaj.util.store.XmlStore;
import ru.chapaj.util.xml.ObjectToXMLConverter;
import ru.dolganov.tool.knowledge.collector.dao.PersistTimer.TimeoutListener;
import model.knowledge.Dir;
import model.knowledge.Image;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.NodeMeta;
import model.knowledge.Note;
import model.knowledge.Root;
import model.knowledge.Tag;
import model.knowledge.TreeLink;
import model.knowledge.role.Parent;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;
import model.tree.TreeSnapshotRoot;

public class DefaultDAOImpl implements DAO {
	
	private static final String DEL_PREFFIX = "#del#";

	XmlStore<Root> metaStore = new XmlStore<Root>(){

		@Override
		protected void config(ObjectToXMLConverter<Root> converter) {
			converter.configureAliases(
					NodeMeta.class,
					Root.class,
					Dir.class,
					LocalLink.class,
					NetworkLink.class,
					Note.class,
					Image.class,
					TreeSnapshotDir.class,
					TreeSnapshotRoot.class,
					TreeSnapshot.class,
					TreeLink.class,
					Tag.class
					);
			
		}
		
	};
	
	String rootFileName = "data.xml";
	NodeMetaObjectsCacheImpl cache = new NodeMetaObjectsCacheImpl();
	
	PersistTimer persistTimer = new PersistTimer(new TimeoutListener(){

		@Override
		public void onTimeout(String dirPath) {
			newSaveThread(dirPath);
		}
		
	},2000);
	
	ArrayList<DAOEventListener> listeners = new ArrayList<DAOEventListener>();
	
	
	String dirPath;
	
	public DefaultDAOImpl(String dirPath) {
		this.dirPath = dirPath;
		getDirRoot(dirPath,true);
		importLC();
		
	}


	@Override
	public Root getRoot() {
		return getDirRoot(dirPath, false);
	}




	@Override
	public void flushMeta() {
		try {
			//metaStore.saveFile(metaFile, metaRoot, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}




	@Override
	public List<NodeMeta> getChildren(Parent parent) {
		if(parent instanceof Root){
			return ((Root)parent).getNodes();
		}
		if (parent instanceof NodeMeta) {
			NodeMeta meta = (NodeMeta) parent;
			Root root = getRoot(meta, false);
			if(root != null) return root.getNodes();
		}
		
		return new ArrayList<NodeMeta>(0);
	}




	@Override
	public void addChild(Parent parent, NodeMeta child) {
		try {
			Root root = null;
			NodeMeta meta = null;
			if (parent instanceof Root) {
				root = (Root) parent;
			}
			else if (parent instanceof NodeMeta) {
				meta = (NodeMeta) parent;
				root = getRoot(meta,true);
			}
			root.getNodes().add(child);
			child.setParent(root);
			
			if(meta != null) for(DAOEventListener l : listeners) l.onAdded(meta,child);
			
			saveRequest(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public NodeMetaObjectsCache getCache() {
		return cache;
	}
	
	
	@Override
	public void addListener(DAOEventListener listener) {
		listeners.add(listener);
	}
	
	
	@Override
	public void delete(NodeMeta node) {
		try {
			Root root = node.getParent();
			root.getNodes().remove(node);
			
			for(DAOEventListener l : listeners) l.onDeleted(node);
			
			cache.remove(node);
			saveRequest(root);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	

	//****************************************************************
	//****************************************************************
	//****************************************************************
	
	
	private void importLC() {
//		System.out.println("import data from link collector...");
//		ImportLinkCollectorData.fill("./import.xml", this);
//		System.out.println("import finised");
	}
	

	private Root getDirRoot(String dirPath,boolean createIfNotExist){
		Root root = cache.getRoot(dirPath);
		if(root != null) return root;

		String filePath = getRootFilePath(dirPath);
		File metaFile = new File(filePath);
		if(!metaFile.exists()){
			if(!createIfNotExist) return null;
			new File(dirPath).mkdir();
			root = new Root();
		}
		else {
			try {
				root = metaStore.loadFile(metaFile);
				for(NodeMeta meta : root.getNodes()){
					meta.setParent(root);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("can't load data from store",e);
			}
		}
		root.setDirPath(dirPath);
		cache.putRoot(root);
		return root;
	}
	
	private void saveRequest(Root root) {
		persistTimer.start(root.getDirPath());
	}
	

	private void newSaveThread(final String dirPath){
		new Thread(){
			@Override
			public void run() {
				save(dirPath);
			}
		}.start();
	}
	
	//private final Object mkDirLock = new Object();
	private final Object saveOneLock = new Object();
	private void save(final String dirPath){
		synchronized (saveOneLock) {
			Root root = cache.getRoot(dirPath);
			if(root != null){
				try {
//					synchronized (mkDirLock) {
//						new File(dirPath).mkdirs();
//					}
					
					//save data.xml
					File dirFile = new File(dirPath);
					dirFile.mkdirs();
					String filePath = getRootFilePath(dirPath);
					System.out.println("saving " + filePath);
					metaStore.saveFile(new File(filePath),root, true);
					
					//sync child folder
					String[] list = dirFile.list(new FilenameFilter(){

						@Override
						public boolean accept(File dir, String name) {
							if(rootFileName.equals(name)) return false;
							if(name.startsWith(DEL_PREFFIX)) return false;
							return true;
						}
						
					});
					ArrayList<String> fileNames = new ArrayList<String>();
					for(String fn : list)fileNames.add(fn);
					
					List<NodeMeta> nodes = root.getNodes();
					for(NodeMeta child : nodes){
						String fileName = child.getName();
						if (child instanceof Dir) {
							String folderPath = getFilePath(dirPath, fileName);
							boolean folderExist = false;
							for (int i = 0; i < fileNames.size(); i++) {
								String name = fileNames.get(i);
								if(name.equals(fileName) && new File(folderPath).isDirectory()){
									folderExist = true;
									fileNames.remove(i);
								}
							}
							if(!folderExist){
								new File(folderPath).mkdir();
							}
						}
						else {
							//TODO sync notes folders
						}
					}
					//delete invalid folders
					for(String name : fileNames){
						String folderPath = getFilePath(dirPath, name);
						File file = new File(folderPath);
						if(file.isDirectory()){
							String newName = generateDeleteFileName(name);
							file.renameTo(new File(getFilePath(dirPath, newName)));
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private String generateDeleteFileName(String name) {
		StringBuilder sb = new StringBuilder().append(DEL_PREFFIX).append(name).append('-').append(System.currentTimeMillis());
		return sb.toString();
	}

	private Root getRoot(NodeMeta meta,boolean createIfNotExist) {
		String path = getDirPath(meta);
		return getDirRoot(path,createIfNotExist);
	}


	private String getDirPath(NodeMeta meta) {
		String parentDirPath = meta.getParent().getDirPath();
		String dirName = meta.getName();
		return getFilePath(parentDirPath, dirName);
	}


	private String getFilePath(String parentDirPath, String dirName) {
		String path = new StringBuilder().append(parentDirPath).append('/').append(dirName).toString();
		return path;
	}
	
	private String getRootFilePath(String dirPath){
		return dirPath+'/'+rootFileName;
	}









}
