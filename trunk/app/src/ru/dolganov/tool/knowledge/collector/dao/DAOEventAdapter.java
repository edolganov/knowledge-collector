package ru.dolganov.tool.knowledge.collector.dao;

import model.knowledge.RootElement;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;

public class DAOEventAdapter implements DAOEventListener {

	@Override
	public void onAdded(RootElement parent, RootElement child) {
	}

	@Override
	public void onDeleted(RootElement node) {
	}
	
	@Override
	public void onUpdated(RootElement node) {
	}
	
	@Override
	public void onAdded(TreeSnapshotDir dir) {
	}
	
	@Override
	public void onAdded(TreeSnapshotDir dir, TreeSnapshot snapshot) {
	}
	
	@Override
	public void onDeleted(TreeSnapshot snapshot) {
	}
	
	@Override
	public void onDeleted(TreeSnapshotDir snapshot) {
	}

}
