package ru.kc.module.snapshots.model.update;

import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;


public class SnapshotMovedToOtherDir extends SnapshotsUpdate {
	
	public final SnapshotDir oldDir;
	public final Snapshot snapshot;
	public final SnapshotDir newDir;
	
	
	public SnapshotMovedToOtherDir(SnapshotDir oldDir, Snapshot snapshot,
			SnapshotDir newDir) {
		super();
		this.oldDir = oldDir;
		this.snapshot = snapshot;
		this.newDir = newDir;
	}
	
	
	
	

}
