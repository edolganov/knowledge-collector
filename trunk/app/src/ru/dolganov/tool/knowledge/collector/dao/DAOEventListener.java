package ru.dolganov.tool.knowledge.collector.dao;

import model.knowledge.NodeMeta;

public interface DAOEventListener {

	void onAdded(NodeMeta parent, NodeMeta child);

	void onDeleted(NodeMeta node);

}
