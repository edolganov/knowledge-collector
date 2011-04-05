package ru.kc.module.snapshots.model.update;

import ru.kc.module.snapshots.model.SnapshotDir;


public class SnapshotDirRenamed extends AbstractSnapshotsUpdate {
	
	public final SnapshotDir dir;
	
	public SnapshotDirRenamed(SnapshotDir dir) {
		super();
		this.dir = dir;
	}
	
	

}
