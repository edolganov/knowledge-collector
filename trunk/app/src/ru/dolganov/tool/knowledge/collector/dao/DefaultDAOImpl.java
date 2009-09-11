package ru.dolganov.tool.knowledge.collector.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
			saveData(dirPath);
		}
		
	},2000);
	
	ArrayList<DAOListener> listeners = new ArrayList<DAOListener>();
	
	
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
			
			if(meta != null) for(DAOListener l : listeners) l.onAdded(meta,child);
			
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
	public void addListener(DAOListener listener) {
		listeners.add(listener);
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

		String filePath = rootFilePath(dirPath);
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
		cache.putRoot(dirPath, root);
		return root;
	}
	
	private void saveRequest(Root root) {
		persistTimer.start(root.getDirPath());
	}
	
	private final Object mkDirLock = new Object();
	private void saveData(final String dirPath){
		new Thread(){
			@Override
			public void run() {
				Root root = cache.getRoot(dirPath);
				if(root != null){
					try {
						synchronized (mkDirLock) {
							new File(dirPath).mkdirs();
						}
						String filePath = rootFilePath(root.getDirPath());
						System.out.println("saving " + filePath);
						metaStore.saveFile(new File(filePath),root, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private Root getRoot(NodeMeta meta,boolean createIfNotExist) {
		String parentDirPath = meta.getParent().getDirPath();
		String dirName = meta.getName();
		String path = new StringBuilder().append(parentDirPath).append('/').append(dirName).toString();
		return getDirRoot(path,createIfNotExist);
	}
	
	private String rootFilePath(String dirPath){
		return dirPath+'/'+rootFileName;
	}







}
