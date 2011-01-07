package ru.chapaj.util.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * HashMap для многопоточного доступа
 * @author jenua.dolganov
 *
 * @param <K>
 * @param <V>
 */
public class SyncHashMap<K,V> extends HashMap<K, V> {
	
	private static final long serialVersionUID = 1L;
	protected ReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Override
	public V get(Object key) {
		lock.readLock().lock();
		try {
			return super.get(key);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public V unsaveGet(Object key) {
		return super.get(key);
	}

	@Override
	public void clear() {
		lock.writeLock().lock();
		try {
			super.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Object clone() {
		lock.readLock().lock();
		try {
			return super.clone();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsKey(Object key) {
		lock.readLock().lock();
		try {
			return super.containsKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsValue(Object value) {
		lock.readLock().lock();
		try {
			return super.containsValue(value);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		lock.readLock().lock();
		try {
			return super.entrySet();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		lock.readLock().lock();
		try {
			return super.isEmpty();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<K> keySet() {
		lock.readLock().lock();
		try {
			return super.keySet();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V put(K key, V value) {
		lock.writeLock().lock();
		try {
			return super.put(key, value);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	protected V unsavePut(K key, V value) {
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		lock.writeLock().lock();
		try {
			super.putAll(m);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public V remove(Object key) {
		lock.writeLock().lock();
		try {
			return super.remove(key);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public int size() {
		lock.readLock().lock();
		try {
			return super.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Collection<V> values() {
		lock.readLock().lock();
		try {
			return super.values();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	

}
