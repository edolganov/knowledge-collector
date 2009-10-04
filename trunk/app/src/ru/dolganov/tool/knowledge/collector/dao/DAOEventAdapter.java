package ru.dolganov.tool.knowledge.collector.dao;

import model.knowledge.NodeMeta;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;

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
	
	@Override
	public void onAdded(TreeSnapshotDir dir) {
	}
	
	@Override
	public void onAdded(TreeSnapshotDir dir, TreeSnapshot snapshot) {
	}

}
