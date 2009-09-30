package ru.dolganov.tool.knowledge.collector.dao;

import model.knowledge.NodeMeta;

/**
 * Глобальный кеш объектов
 * @author jenua.dolganov
 *
 */
public interface NodeMetaObjectsCache {

	/**
	 * положить для данной ноды по ключу объект
	 * @param node
	 * @param key
	 * @param object
	 */
	void put(NodeMeta node, String key, Object object);

	/**
	 * получить объект по ключу для данной ноды
	 * @param <T>
	 * @param node
	 * @param key
	 * @param clazz
	 * @return
	 */
	<T> T get(NodeMeta node, String key, Class<T> clazz);

}