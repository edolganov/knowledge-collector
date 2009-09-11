package ru.dolganov.tool.knowledge.collector.dao;

import model.knowledge.NodeMeta;

/**
 * Глобальный кеш объектов
 * @author jenua.dolganov
 *
 */
public interface NodeMetaObjectsCache {

	void put(NodeMeta node, String key, Object object);

	<T> T get(NodeMeta node, String key, Class<T> clazz);

}