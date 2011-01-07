package model.tree;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tree-snapshot-root")
public class TreeSnapshotRoot {
	
	private TreeSnapshot lastTreeState;
	
	private List<TreeSnapshot> snapshots;
	
	private List<TreeSnapshotDir> snaphotDirs;
	
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
