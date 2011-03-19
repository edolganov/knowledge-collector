package ru.kc.module.snapshots.model;

import java.util.List;

public class SnapshotDirBean  {
	
	
	private boolean open;
	private String name;
	private List<SnapshotBean> snapshots;
	

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isOpen() {
		return open;
	}
	
	public void setSnapshots(List<SnapshotBean> snapshots) {
		this.snapshots = snapshots;
	}

	public List<SnapshotBean> getSnapshots() {
		return snapshots;
	}
}
