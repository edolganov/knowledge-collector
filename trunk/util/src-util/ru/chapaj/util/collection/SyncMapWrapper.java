package ru.chapaj.util.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Оболочка над картой для многопоточного доступа
 * @author jenua.dolganov
 *
 * @param <K>
 * @param <V>
 */
public class SyncMapWrapper<K,V> implements Map<K, V> {
	
	private static final long serialVersionUID = 1L;
	ReadWriteLock lock = new ReentrantReadWriteLock();
	Map<K, V> map;
	
	public SyncMapWrapper(Map<K, V> map) {
		this.map = map;
	}
	
	@Override
	public V get(Object key) {
		lock.readLock().lock();
		try {
			return map.get(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void clear() {
		lock.writeLock().lock();
		try {
			map.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public boolean containsKey(Object key) {
		lock.readLock().lock();
		try {
			return map.containsKey(key);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsValue(Object value) {
		lock.readLock().lock();
		try {
			return map.containsValue(value);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		lock.readLock().lock();
		try {
			return map.entrySet();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		lock.readLock().lock();
		try {
			return map.isEmpty();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Set<K> keySet() {
		lock.readLock().lock();
		try {
			return map.keySet();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public V put(K key, V value) {
		lock.writeLock().lock();
		try {
			return map.put(key, value);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		lock.writeLock().lock();
		try {
			map.putAll(m);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public V remove(Object key) {
		lock.writeLock().lock();
		try {
			return map.remove(key);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public int size() {
		lock.readLock().lock();
		try {
			return map.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Collection<V> values() {
		lock.readLock().lock();
		try {
			return map.values();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	

}
