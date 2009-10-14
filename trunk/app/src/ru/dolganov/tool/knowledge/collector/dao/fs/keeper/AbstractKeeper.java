package ru.dolganov.tool.knowledge.collector.dao.fs.keeper;

import ru.dolganov.tool.knowledge.collector.dao.fs.NodeMetaObjectsCacheImpl;
import ru.dolganov.tool.knowledge.collector.dao.fs.tools.DelManager;

public abstract class AbstractKeeper {
	
	protected DelManager delManager = new DelManager();
	
	protected NodeMetaObjectsCacheImpl cache;

	public AbstractKeeper(NodeMetaObjectsCacheImpl cache) {
		super();
		this.cache = cache;
	}
	
	
	

}
