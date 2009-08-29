package ru.dolganov.tool.knowledge.collector.dao;

import java.util.List;

import model.knowledge.NodeMeta;
import model.knowledge.Root;
import model.knowledge.role.Parent;

public interface DAO {
	
	Root getRoot();

	void flushMeta();

	List<NodeMeta> getChildren(Parent parent);
	
	void addChild(Parent parent, NodeMeta child);

}
