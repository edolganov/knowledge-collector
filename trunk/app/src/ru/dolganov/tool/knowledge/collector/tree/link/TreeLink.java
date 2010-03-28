package ru.dolganov.tool.knowledge.collector.tree.link;

import java.util.ArrayList;
import java.util.List;

import ru.chapaj.util.collection.SyncHashMap;

import model.knowledge.NodeLink;
import model.knowledge.RootElement;

public class TreeLink {
	
	static class Cache extends SyncHashMap<String, List<TreeLink>> {
		
		public void put(String key, TreeLink value) {
			lock.writeLock().lock();
			try {
				List<TreeLink> list = unsaveGet(key);
				if(list == null) {
					list = new ArrayList<TreeLink>();
					unsavePut(key, list);
				}
				list.add(value);
				
			} finally {
				lock.writeLock().unlock();
			}
		}
	}
	
	private static final Cache cache = new Cache();

	NodeLink nodeLink;
	RootElement node;
	
	public TreeLink(NodeLink nodeLink, RootElement node) {
		this.node = node;
		this.nodeLink = nodeLink;
		cache.put(node.getUuid(),this);
	}

}
