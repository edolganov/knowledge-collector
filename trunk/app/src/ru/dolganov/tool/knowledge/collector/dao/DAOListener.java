package ru.dolganov.tool.knowledge.collector.dao;

import model.knowledge.NodeMeta;

public interface DAOListener {

	void onAdded(NodeMeta parent, NodeMeta child);

}
