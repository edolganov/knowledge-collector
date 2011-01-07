package ru.dolganov.tool.knowledge.collector.dao;

import model.knowledge.RootElement;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;

public interface DAOEventListener {

	void onAdded(RootElement parent, RootElement child);

	void onDeleted(RootElement node);

	void onUpdated(RootElement node);
	
	void onAdded(TreeSnapshotDir dir);

	void onAdded(TreeSnapshotDir dir, TreeSnapshot snapshot);
	
	void onDeleted(TreeSnapshot snapshot);
	
	void onDeleted(TreeSnapshotDir snapshot);

}
