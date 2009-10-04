package ru.dolganov.tool.knowledge.collector.dao;

import model.knowledge.NodeMeta;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;

public interface DAOEventListener {

	void onAdded(NodeMeta parent, NodeMeta child);

	void onDeleted(NodeMeta node);

	void onUpdated(NodeMeta node);
	
	void onAdded(TreeSnapshotDir dir);

	void onAdded(TreeSnapshotDir dir, TreeSnapshot snapshot);

}
