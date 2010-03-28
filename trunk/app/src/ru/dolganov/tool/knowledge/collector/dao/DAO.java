package ru.dolganov.tool.knowledge.collector.dao;

import java.util.List;
import java.util.Map;

import model.knowledge.RootElement;
import model.knowledge.Root;
import model.knowledge.role.Parent;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;

public interface DAO {
	
	Root getRoot();

	//Node
	List<RootElement> getChildren(Parent parent);
	
	boolean addChild(Parent parent, RootElement child);
	
	boolean addChild(Parent parent, RootElement child,Map<String, String> params);
	
	NodeObjectsCache getCache();
	
	void addListener(DAOEventListener listener);

	void delete(RootElement node);

	void update(RootElement node, Map<String, String> params);

	Map<String,Object> getExternalData(RootElement ob);

	void merge(Root object);
	
	void merge(Root root, boolean immediately);
	
	RootElement find(String rootUuid, String nodeUuid);
	
	
	//Snaps
	void add(TreeSnapshot object, Map<String, Object> params);
	
	void add(TreeSnapshotDir object);

	void delete(TreeSnapshotDir parent, TreeSnapshot node);

	void delete(TreeSnapshotDir ob);

	void update(TreeSnapshot snap);



}
