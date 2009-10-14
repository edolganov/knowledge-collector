package ru.dolganov.tool.knowledge.collector.dao.fs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ru.dolganov.tool.knowledge.collector.dao.NodeMetaObjectsCache;

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
	
	// [14.10.2009] jenua.dolganov: более корректная модель для кеширования рутов 
	// строим дерево из рутов, храня название их папки (или весь путь для корневого рута)
	// при удалении, перемещении, переименовании не нужно пробегать весь кеш - достаточно
	// только исправить один элемент
	static class RootCache {
		Root root;
		List<Root> chidren = new LinkedList<Root>();
		Root parent;
		String dirName;
	}

	
	
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

	/**
	 * Не очень красивое (полный перебор), но простое решение обновления кеша
	 * @param pathPattern
	 */
	public void deleteAllRoots(String pathPattern) {
		lock.writeLock().lock();
		try {
			pathPattern = pathPattern.replace('\\', '/');
			LinkedList<String> keys = new LinkedList<String>();
			for(String key : rootsMap.keySet())
				if(key.startsWith(pathPattern))keys.add(key);
			for(String key : keys){
				Root root = rootsMap.remove(key);
				for(NodeMeta node : root.getNodes()){
					objectsMap.remove(getNodeKey(node));
				}
			}
			
		} finally {
			lock.writeLock().unlock();
		}
		
	}
	
	/**
	 * Не очень красивое (полный перебор), но простое решение обновления кеша
	 * @param pathPattern
	 */
	public void renameAllRoots(String oldPathPattern, String newPathPattern) {
		lock.writeLock().lock();
		try {
			oldPathPattern = oldPathPattern.replace('\\', '/');
			newPathPattern = newPathPattern.replace('\\', '/');
			int oldPatternLenth = oldPathPattern.length();
			LinkedList<String> keys = new LinkedList<String>();
			for(String key : rootsMap.keySet())
				if(key.startsWith(oldPathPattern))keys.add(key);
			for(String key : keys){
				Root root = rootsMap.remove(key);
				String dirPath = root.getDirPath();
				String newPath = newPathPattern;
				String part = dirPath.substring(oldPatternLenth);
				if(part.length() > 0) newPath = newPath + part;
				root.setDirPath(newPath);
				rootsMap.put(root.getDirPath(), root);
			}
		} finally {
			lock.writeLock().unlock();
		}
		
		
	}
	

	

}
