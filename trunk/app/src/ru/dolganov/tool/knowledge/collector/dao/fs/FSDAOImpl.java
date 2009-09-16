package ru.dolganov.tool.knowledge.collector.dao.fs;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.chapaj.util.store.XmlStore;
import ru.chapaj.util.xml.ObjectToXMLConverter;
import ru.dolganov.tool.knowledge.collector.dao.DAO;
import ru.dolganov.tool.knowledge.collector.dao.DAOEventListener;
import ru.dolganov.tool.knowledge.collector.dao.NodeMetaObjectsCache;
import ru.dolganov.tool.knowledge.collector.dao.fs.PersistTimer.TimeoutListener;
import ru.dolganov.tool.knowledge.collector.dao.fs.keeper.DirKeeper;
import ru.dolganov.tool.knowledge.collector.dao.fs.keeper.TextKeeper;
import ru.dolganov.tool.knowledge.collector.model.HasNodeMetaParams;
import model.knowledge.Dir;
import model.knowledge.Image;
import model.knowledge.Link;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.NodeMeta;
import model.knowledge.Note;
import model.knowledge.Root;
import model.knowledge.Tag;
import model.knowledge.TextData;
import model.knowledge.TreeLink;
import model.knowledge.role.Parent;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;
import model.tree.TreeSnapshotRoot;

public class FSDAOImpl implements DAO, HasNodeMetaParams {

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
		public void onTimeout(String dirPath,Map<SaveOps, Object[]> saveOps) {
			newSaveThread(dirPath,saveOps);
		}
		
	},2000);
	
	ArrayList<DAOEventListener> listeners = new ArrayList<DAOEventListener>();
	
	DirKeeper dirKeeper = new DirKeeper();
	TextKeeper textKeeper = new TextKeeper();
	
	
	String dirPath;
	
	public FSDAOImpl(String dirPath) {
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
			
			HashMap<SaveOps, Object[]> saveOps = new HashMap<SaveOps, Object[]>();
			if(meta != null) {
				if(child instanceof Dir){
					saveOps.put(SaveOps.dirFlag, null);
					saveOps.put(SaveOps.create, new Object[]{child});
				}
				for(DAOEventListener l : listeners) l.onAdded(meta,child);
			}
			
			saveRequest(root,saveOps);
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
			
			HashMap<SaveOps, Object[]> saveOps = new HashMap<SaveOps, Object[]>();
			if(node instanceof Dir){
				saveOps.put(SaveOps.dirFlag, null);
				saveOps.put(SaveOps.delete, new Object[]{node});
			}
			else if(node instanceof TextData){
				saveOps.put(SaveOps.textFlag, null);
				saveOps.put(SaveOps.delete, new Object[]{node});
			}
			
			for(DAOEventListener l : listeners) l.onDeleted(node);
			
			cache.remove(node);
			saveRequest(root,saveOps);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void update(NodeMeta node, Map<String, String> params) {
		try {
			String name = params.get(Params.name.toString());
			HashMap<SaveOps, Object[]> saveOps = new HashMap<SaveOps, Object[]>();
			if(name == null) return;
			
			boolean isNewName = false;
			String oldName = node.getName();
			if(!oldName.equals(name)){
				node.setName(name);
				isNewName = true;
			}
			
			if (node instanceof Dir) {
				saveOps.put(SaveOps.dirFlag, null);
				Dir dir = (Dir) node;
				dir.setDescription(params.get(Params.description.toString()));
				if(isNewName) saveOps.put(SaveOps.rename, new Object[]{node,oldName});
			}
			else if(node instanceof Link){
				Link link = (Link) node;
				link.setUrl(params.get(Params.url.toString()));
				link.setDescription(params.get(Params.description.toString()));
			}
			else if(node instanceof TextData){
				saveOps.put(SaveOps.textFlag, null);
				saveOps.put(SaveOps.update, new Object[]{node,params.get(Params.text.toString())});
				if(isNewName) saveOps.put(SaveOps.rename, new Object[]{node,oldName});
				textKeeper.beforeUpdate(node,params);
			}
			
			for(DAOEventListener l : listeners) l.onUpdated(node);
			
			saveRequest(node.getParent(),saveOps);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
	}


	HashMap<String, Object> emptyExternalData = new HashMap<String, Object>(0);
	@Override
	public Map<String, Object> getExternalData(NodeMeta ob) {
		if(ob instanceof TextData) return textKeeper.getExternalData(ob);
		return emptyExternalData;
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
	
	private void saveRequest(Root root, Map<SaveOps, Object[]> saveOps) {
		persistTimer.start(root.getDirPath(),saveOps);
	}
	

	private void newSaveThread(final String dirPath, final Map<SaveOps, Object[]> saveOps){
		new Thread(){
			@Override
			public void run() {
				save(dirPath,saveOps);
			}
		}.start();
	}
	
	//private final Object mkDirLock = new Object();
	private final Object saveOneLock = new Object();
	private void save(final String dirPath, Map<SaveOps, Object[]> saveOps){
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
					
					//do save ops
					if(saveOps != null){
						System.out.println("do save ops");
						if(saveOps.containsKey(SaveOps.dirFlag)){
							dirKeeper.manage(dirFile, saveOps);
						}
						else if(saveOps.containsKey(SaveOps.textFlag)){
							textKeeper.manage(dirFile, saveOps);
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private Root getRoot(NodeMeta meta,boolean createIfNotExist) {
		String path = getDirPath(meta);
		return getDirRoot(path,createIfNotExist);
	}


	private String getDirPath(NodeMeta meta) {
		String parentDirPath = meta.getParent().getDirPath();
		String dirName = meta.getName();
		return DAOUtil.getFilePath(parentDirPath, dirName);
	}



	
	private String getRootFilePath(String dirPath){
		return dirPath+'/'+rootFileName;
	}















}
