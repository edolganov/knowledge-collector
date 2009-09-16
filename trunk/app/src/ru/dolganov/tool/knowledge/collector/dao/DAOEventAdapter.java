package ru.dolganov.tool.knowledge.collector.dao;

import model.knowledge.NodeMeta;

public abstract class DAOEventAdapter implements DAOEventListener {

	@Override
	public void onAdded(NodeMeta parent, NodeMeta child) {
	}

	@Override
	public void onDeleted(NodeMeta node) {

	}
	
	@Override
	public void onUpdated(NodeMeta node) {
		
	}

}
