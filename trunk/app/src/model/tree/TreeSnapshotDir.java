package model.tree;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tree-snapshot-dir")
public class TreeSnapshotDir {
	
	String name;
	Boolean opened;
	ArrayList<TreeSnapshot> snapshots;
	

	public ArrayList<TreeSnapshot> getSnapshots() {
		if(snapshots == null) snapshots = new ArrayList<TreeSnapshot>();
		return snapshots;
	}

	public void setSnapshots(ArrayList<TreeSnapshot> snapshots) {
		this.snapshots = snapshots;
	}

	public TreeSnapshotDir() {
		super();
	}

	public TreeSnapshotDir(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name != null ? name + " ("+getSnapshots().size()+")" : "noname dir";
	}

	public boolean isOpened() {
		if(opened == null) opened = false;
		return opened;
	}

	public void setOpened(Boolean opened) {
		this.opened = opened;
	}

}
