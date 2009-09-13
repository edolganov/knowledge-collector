package ru.dolganov.tool.knowledge.collector.dao;

import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import model.knowledge.NodeMeta;
import model.knowledge.Root;

/**
 * Глобальный кеш объектов
 * @author jenua.dolganov
 *
 */
public class NodeMetaObjectsCacheImpl implements NodeMetaObjectsCache {
	
	
	
	ReadWriteLock lock = new ReentrantReadWriteLock();
	
	HashMap<String, HashMap<String, Object>> objectsMap = new HashMap<String, HashMap<String, Object>>();
	HashMap<String, Root> rootsMap = new HashMap<String, Root>();

	
	
	public void put(NodeMeta node, String key, Object object){
		lock.writeLock().lock();
		try {
			HashMap<String, Object> nodeObjects = getNodeObjects(node);
			nodeObjects.put(key, object);
		} finally {
			lock.writeLock().unlock();
		}

	}
	
	public void putRoot(Root root){
		lock.writeLock().lock();
		try {
			rootsMap.put(root.getDirPath(), root);
		} finally {
			lock.writeLock().unlock();
		}
	}

	

	@SuppressWarnings("unchecked")
	public <T> T get(NodeMeta node, String key, Class<T> clazz){
		lock.readLock().lock();
		try {
			HashMap<String, Object> nodeObjects = getNodeObjects(node);
			return (T)nodeObjects.get(key);
		} finally {
			lock.readLock().unlock();
		}

	}
	
	private HashMap<String, Object> getNodeObjects(NodeMeta node) {
		String key = getNodeKey(node);
		HashMap<String, Object> out = objectsMap.get(key);
		if(out == null){
			out = new HashMap<String, Object>();
			objectsMap.put(key, out);
		}
		return out;
	}

	private String getNodeKey(NodeMeta node) {
		return node.getUuid();
	}
	
	
	public Root getRoot(String path){
		lock.readLock().lock();
		try {
			return rootsMap.get(path);
		} finally {
			lock.readLock().unlock();
		}
	}

	public void remove(NodeMeta node) {
		lock.writeLock().lock();
		try {
			objectsMap.remove(getNodeKey(node));
		} finally {
			lock.writeLock().unlock();
		}
	}
	

	

}
