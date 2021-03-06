package ru.dolganov.tool.knowledge.collector.dao.fs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.knowledge.Dir;
import model.knowledge.Link;
import model.knowledge.Node;
import model.knowledge.RootElement;
import model.knowledge.Root;
import model.knowledge.TextData;
import model.knowledge.role.Parent;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;
import ru.chapaj.util.lang.PackageExplorer;
import ru.chapaj.util.store.XmlStore;
import ru.chapaj.util.xml.ObjectToXMLConverter;
import ru.dolganov.tool.knowledge.collector.dao.DAO;
import ru.dolganov.tool.knowledge.collector.dao.DAOEventListener;
import ru.dolganov.tool.knowledge.collector.dao.NodeObjectsCache;
import ru.dolganov.tool.knowledge.collector.dao.fs.PersistTimer.TimeoutListener;
import ru.dolganov.tool.knowledge.collector.dao.fs.keeper.DirKeeper;
import ru.dolganov.tool.knowledge.collector.dao.fs.keeper.SnapshotKeeper;
import ru.dolganov.tool.knowledge.collector.dao.fs.keeper.TextKeeper;
import ru.dolganov.tool.knowledge.collector.model.HasNodeMetaParams;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class FSDAOImpl implements DAO, HasNodeMetaParams {

	XmlStore<Root> metaStore = new XmlStore<Root>(){

		@Override
		protected void config(final ObjectToXMLConverter<Root> converter) {
			//System.out.println("FSDAOImpl.config");
			PackageExplorer.find("model", new PackageExplorer.Callback(){

				@Override
				public void found(Class<?> clazz) {
					if(clazz.getAnnotation(XStreamAlias.class) != null){
						//System.out.println(clazz);
						converter.configureAliases(clazz);
					}
				}
				
			});
//			converter.configureAliases(
//					NodeMeta.class,
//					Root.class,
//					Dir.class,
//					LocalLink.class,
//					NetworkLink.class,
//					Note.class,
//					Image.class,
//					TreeSnapshotDir.class,
//					TreeSnapshotRoot.class,
//					TreeSnapshot.class,
//					TreeLink.class,
//					Tag.class
//					);
			
		}
		
	};
	
	String rootFileName = "data.xml";
	NodeMetaObjectsCacheImpl cache = new NodeMetaObjectsCacheImpl();
	
	int timeOutDelay = 1000;
	PersistTimer persistTimer = new PersistTimer(new TimeoutListener(){

		@Override
		public void onTimeout(String dirPath,Map<SaveOps, Object[]> saveOps,List<Map<SaveOps,Object[]>> oldSaveOps) {
			newSaveThread(dirPath,saveOps,oldSaveOps);
		}
		
	},timeOutDelay);
	
	ArrayList<DAOEventListener> listeners = new ArrayList<DAOEventListener>();
	
	DirKeeper dirKeeper = new DirKeeper(cache);
	TextKeeper textKeeper = new TextKeeper(cache);
	SnapshotKeeper snapshotKeeper = new SnapshotKeeper(cache);
	
	
	String dirPath;
	
	public FSDAOImpl(String dirPath) {
		dirPath= dirPath.replace('\\', '/');
		this.dirPath = dirPath;
		
		getDirRoot(dirPath,true);
		importLC();
		
	}


	@Override
	public Root getRoot() {
		return getDirRoot(dirPath, false);
	}




	@Override
	public List<RootElement> getChildren(Parent parent) {
		if(parent instanceof Root){
			return ((Root)parent).getNodes();
		}
		if (parent instanceof Node) {
			Node meta = (Node) parent;
			Root root = getRoot(meta, false);
			if(root != null) return root.getNodes();
		}
		
		return Collections.emptyList();
	}




	@Override
	public boolean addChild(Parent parent, RootElement child) {
		return addChild(parent, child, null);
	}
	
	@Override
	public boolean addChild(Parent parent, RootElement child,Map<String, String> params){
		if(parent == null || child == null) return false;
		
		if(child instanceof Node){
			new DAOCheck(this).checkNoExist(parent,(Node) child);
		}
			
		Root root = null;
		Node parentNode = null;
		if (parent instanceof Root) {
			root = (Root) parent;
		}
		else if (parent instanceof Node) {
			parentNode = (Node) parent;
			root = getRoot(parentNode,true);
			if(root == null) return false;
		}
		Root oldRoot = child.getParent();
		if(oldRoot != null) oldRoot.getNodes().remove(child);
		root.getNodes().add(child);
		child.setParent(root);
		
		Map<SaveOps, Object[]> saveOps = new HashMap<SaveOps, Object[]>();
		if(parentNode != null) {
			if(params == null) params = new HashMap<String, String>(0);
			if(child instanceof Dir){
				saveOps.put(SaveOps.dirFlag, null);
				if(oldRoot == null)saveOps.put(SaveOps.create, new Object[]{child});
				else saveOps.put(SaveOps.move, new Object[]{child});
			}
			else if(child instanceof TextData){
				saveOps.put(SaveOps.textFlag, null);
				if(oldRoot == null)saveOps.put(SaveOps.update, new Object[]{child,params.get(Params.text.toString())});
				else saveOps.put(SaveOps.move, new Object[]{child});
			}
		}
		if(oldRoot == null){
			//saveRequest(root,saveOps);
			if(!save(saveOps, null, root))return false;
			if(parentNode != null) for(DAOEventListener l : listeners) l.onAdded(parentNode,child);
			return true;
		}
		else {
			boolean successed = save(saveOps, null, oldRoot);
			if(successed) successed = save(null,null,root);
			if(!successed) return false;
			
			if(parentNode != null) for(DAOEventListener l : listeners) l.onAdded(parentNode,child);
			return true;
		}
			
	}
	
	@Override
	public NodeObjectsCache getCache() {
		return cache;
	}
	
	
	@Override
	public void addListener(DAOEventListener listener) {
		listeners.add(listener);
	}
	
	
	@Override
	public void delete(RootElement node) {
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
			
			//saveRequest(root,saveOps);
			if(!save(saveOps, null, root))return;
			
			for(DAOEventListener l : listeners) l.onDeleted(node);
			cache.remove(node);
			
		} catch (RuntimeException e) {
			flushToFile(e);
		}
	}
	
	@Override
	public void delete(TreeSnapshotDir parent, TreeSnapshot node){
		parent.getSnapshots().remove(node);
		if(save(null, null, getRoot()))
			for(DAOEventListener l : listeners) l.onDeleted(node);
	}
	
	public void delete(TreeSnapshotDir dir){
		Root root = getRoot();
		root.getTreeSnapshots().getSnaphotDirs().remove(dir);
		if(save(null, null, root))
			for(DAOEventListener l : listeners) l.onDeleted(dir);
	}
	
	
	@Override
	public void update(RootElement node_, Map<String, String> params) {
		if(node_ instanceof Node)
		{
			Node node = (Node) node_;
			HashMap<SaveOps, Object[]> saveOps = new HashMap<SaveOps, Object[]>();
			
			//name of the node
			boolean isNewName = false;
			String oldName = node.getName();
			String name = params.get(Params.name.toString());
			if(name != null) {
				
				if(!oldName.equals(name)){
					new DAOCheck(this).checkNoExist(node.getParent(), name, node.getClass());
					node.setName(name);
					isNewName = true;
				}
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
			
			//saveRequest(node.getParent(),saveOps);
			if(!save(saveOps, null, node.getParent()))return;
			for(DAOEventListener l : listeners) l.onUpdated(node);
		}
		
	}


	HashMap<String, Object> emptyExternalData = new HashMap<String, Object>(0);
	@Override
	public Map<String, Object> getExternalData(RootElement ob) {
		if(ob instanceof TextData) return textKeeper.getExternalData((TextData)ob);
		return emptyExternalData;
	}
	
	@Override
	public void add(TreeSnapshot ob, Map<String, Object> params) {
		if(ob == null) return;
		Root root = getRoot();
		TreeSnapshot snapshot = (TreeSnapshot)ob;
		boolean persisted = snapshotKeeper.persist(root, snapshot, params);
		if(persisted){
			TreeSnapshotDir dir = snapshotKeeper.getSnapDir(root, params);
			saveRequest(root,null);
			for(DAOEventListener l : listeners) l.onAdded(dir,snapshot);
		}
	}
	
	@Override
	public void add(TreeSnapshotDir ob) {
		if(ob == null) return;
		String name = ob.getName();
		if(name == null) return;
		Root root = getRoot();
		TreeSnapshotDir dir = snapshotKeeper.persist(root, name);
		if(dir != null){
			saveRequest(root,null);
			for(DAOEventListener l : listeners) l.onAdded(dir);
		}
		
	}
	
	@Override
	public void merge(Root ob) {
		if(ob == null) return;
		saveRequest(ob,null);
	}
	
	@Override
	public void merge(Root root, boolean immediately) {
		save(null, null, root);
	}
	
	@Override
	public RootElement find(String rootUuid, String nodeUuid) {
		if(rootUuid == null || nodeUuid == null) return null;
		
		Root root = cache.getRootByUuid(rootUuid);
		if(root == null) return null;
		
		List<RootElement> nodes = root.getNodes();
		for (RootElement rootElement : nodes) {
			if(rootElement.getUuid().equals(nodeUuid)){
				return rootElement;
			}
		}
		return null;
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
				//add uuid to the root if it not set yet
				if(root.getUnsafeUuid() == null){
					root.getUuid();
					metaStore.saveFile(metaFile, root, true);
				}
				
				for(RootElement meta : root.getNodes()){
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
//		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//		int length = stackTrace.length;
//		if(length > 6)length=6;
//		for (int i = 0; i < length; i++) {
//			System.out.println(stackTrace[i]);
//		}
		persistTimer.start(root.getDirPath(),saveOps);
	}
	

	private void newSaveThread(final String dirPath, final Map<SaveOps, Object[]> saveOps,final List<Map<SaveOps,Object[]>> oldSaveOps){
		new Thread(){
			@Override
			public void run() {
				save(dirPath,saveOps,oldSaveOps);
			}
		}.start();
	}
	
	//private final Object mkDirLock = new Object();
	//private final Object saveOneLock = new Object();
	private void save(final String dirPath, Map<SaveOps, Object[]> saveOps,List<Map<SaveOps,Object[]>> oldSaveOps){
		Root root = cache.getRoot(dirPath);
		if(root != null) save(saveOps,oldSaveOps, root);
	}


	private synchronized boolean save(Map<SaveOps, Object[]> saveOps,List<Map<SaveOps, Object[]>> oldSaveOps, Root root) {
		String dirPath = root.getDirPath();
		try {
//					synchronized (mkDirLock) {
//						new File(dirPath).mkdirs();
//					}
			
			//save data.xml
			File dirFile = new File(dirPath);
			dirFile.mkdirs();
			
			//do save ops
			//System.out.println("do save ops");
			//old
			if(oldSaveOps != null){
				for(Map<SaveOps,Object[]> ops : oldSaveOps) doSaveOps(ops, dirFile);
			}
			//current
			doSaveOps(saveOps, dirFile);
			//System.out.println("done");
			
			String filePath = getRootFilePath(dirPath);
			System.out.println("saving " + filePath);
			metaStore.saveFile(new File(filePath),root, true);
			return true;
			
		} catch (Exception e) {
			flushToFile(e);
			return false;
		}
	}


	private void doSaveOps(Map<SaveOps, Object[]> saveOps, File dirFile) throws Exception {
		if(saveOps != null){
			//test log
//			for(SaveOps op : saveOps.keySet()){
//				Object[] obs = saveOps.get(op);
//				StringBuilder sb = new StringBuilder();
//				if(obs != null) for(Object ob : obs) sb.append(ob).append(' ');
//				System.out.println(op + ": " + sb.toString());
//			}
			if(saveOps.containsKey(SaveOps.dirFlag)){
				dirKeeper.manage(dirFile, saveOps);
			}
			else if(saveOps.containsKey(SaveOps.textFlag)){
				textKeeper.manage(dirFile, saveOps);
			}

		}
	}


	public Root getRoot(Node meta,boolean createIfNotExist) {
		String path = getDirPath(meta);
		if(path == null) return null;
		return getDirRoot(path,createIfNotExist);
	}


	private String getDirPath(Node meta) {
		String parentDirPath = meta.getParent().getDirPath();
		String dirName = null;
		if(meta instanceof Dir){
			dirName = dirKeeper.getDirName(meta);
		}
		else if(meta instanceof TextData){
			dirName = textKeeper.getDirName(meta);
		}
		
		if(dirName == null) return null;
		return DU.getFilePath(parentDirPath, dirName);
	}



	
	private String getRootFilePath(String dirPath){
		return dirPath+'/'+rootFileName;
	}
	

	private void flushToFile(Exception e) {
		try {
			File file = new File(this.dirPath+"/`error`"+System.currentTimeMillis());
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			e.printStackTrace(new PrintStream(fos));
			fos.flush();
			fos.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}


	@Override
	public void update(TreeSnapshot snap) {
		save(null, null, getRoot());
	}










}
