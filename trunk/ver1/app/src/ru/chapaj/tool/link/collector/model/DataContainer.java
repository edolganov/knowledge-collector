package ru.chapaj.tool.link.collector.model;

import java.util.ArrayList;
import java.util.List;

import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("container")
public class DataContainer {
	
	Dir root;
	
	TreeSnapshot lastTreeState;
	
	List<TreeSnapshot> snapshots;
	
	List<TreeSnapshotDir> snaphotDirs;
	

	public Dir getRoot() {
		return root;
	}

	public void setRoot(Dir root) {
		this.root = root;
	}

	public TreeSnapshot getLastTreeState() {
		return lastTreeState;
	}

	public void setLastTreeState(TreeSnapshot lastTreeState) {
		this.lastTreeState = lastTreeState;
	}

	public List<TreeSnapshot> getSnapshots() {
		if(snapshots == null)snapshots = new ArrayList<TreeSnapshot>();
		return snapshots;
	}

	public void setSnapshots(List<TreeSnapshot> snapshots) {
		this.snapshots = snapshots;
	}

	public List<TreeSnapshotDir> getSnaphotDirs() {
		if(snaphotDirs == null)snaphotDirs = new ArrayList<TreeSnapshotDir>();
		return snaphotDirs;
	}

	public void setSnaphotDirs(List<TreeSnapshotDir> snaphotDirs) {
		this.snaphotDirs = snaphotDirs;
	}

}
