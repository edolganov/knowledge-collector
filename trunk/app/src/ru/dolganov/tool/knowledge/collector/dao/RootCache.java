package ru.dolganov.tool.knowledge.collector.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import model.knowledge.Root;

/**
 * Используем один глобальный класс кешей
 * @author jenua.dolganov
 *
 */
@Deprecated
public class RootCache {
	
	Map<String, Root> map = new HashMap<String, Root>();
	ReadWriteLock lock = new ReentrantReadWriteLock();
	
	
	public Root get(String path){
		lock.readLock().lock();
		try {
			return map.get(path);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public void put(String path, Root root){
		lock.writeLock().lock();
		try {
			map.put(path, root);
		} finally {
			lock.writeLock().unlock();
		}
	}
	

}
