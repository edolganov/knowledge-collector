package ru.dolganov.tool.knowledge.collector.tree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.NodeMeta;


/**
 * Используем один глобальный класс кешей
 * @author jenua.dolganov
 *
 */
@Deprecated
public class TreeNodeCache {
	
	HashMap<String, DefaultMutableTreeNode> map = new HashMap<String, DefaultMutableTreeNode>();
	
	public synchronized void put(NodeMeta node, DefaultMutableTreeNode treeNode){
		String key = getKey(node);
		map.put(key, treeNode);
	}

	
	public DefaultMutableTreeNode get(NodeMeta node){
		return map.get(getKey(node));
	}
	
	private String getKey(NodeMeta node) {
		return node.getParent().getDirPath() + node.getUuid();
	}

}
