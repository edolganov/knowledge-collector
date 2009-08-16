package model.knowledge;

import model.tree.TreeSnapshotRoot;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("container")
public class Root {
	
	private Dir root;
	private TreeSnapshotRoot treeSnapshots;

	public TreeSnapshotRoot getTreeSnapshots() {
		if(treeSnapshots == null) treeSnapshots = new TreeSnapshotRoot();
		return treeSnapshots;
	}

	public void setTreeSnapshots(TreeSnapshotRoot treeSnapshots) {
		this.treeSnapshots = treeSnapshots;
	}

	public Dir getRoot() {
		return root;
	}

	public void setRoot(Dir root) {
		this.root = root;
	}

}
