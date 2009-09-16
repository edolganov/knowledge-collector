package ru.dolganov.tool.knowledge.collector.dao;

import java.util.List;
import java.util.Map;

import model.knowledge.NodeMeta;
import model.knowledge.Root;
import model.knowledge.role.Parent;

public interface DAO {
	
	Root getRoot();

	void flushMeta();

	List<NodeMeta> getChildren(Parent parent);
	
	void addChild(Parent parent, NodeMeta child);
	
	NodeMetaObjectsCache getCache();
	
	void addListener(DAOEventListener listener);

	void delete(NodeMeta node);

	void update(NodeMeta node, Map<String, String> params);

	Map<String,Object> getExternalData(NodeMeta ob);

}
