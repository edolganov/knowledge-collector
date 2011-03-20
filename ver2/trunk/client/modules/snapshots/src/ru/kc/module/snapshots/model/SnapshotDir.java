package ru.kc.module.snapshots.model;

import java.util.ArrayList;
import java.util.List;

public class SnapshotDir  {
	
	
	private boolean open;
	private String name;
	private List<Snapshot> snapshots = new ArrayList<Snapshot>();
	
	public void add(Snapshot snapshot){
		snapshots.add(snapshot);
	}
	

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
	
	public void setSnapshots(List<Snapshot> snapshots) {
		this.snapshots = snapshots;
	}

	public List<Snapshot> getSnapshots() {
		return snapshots;
	}
}
