package ru.kc.module.snapshots.model.update;

import ru.kc.module.snapshots.model.SnapshotDir;


public class SnapshotDirCreated extends AbstractSnapshotsUpdate {
	
	public final SnapshotDir dir;
	public final int index;
	
	public SnapshotDirCreated(SnapshotDir dir, int index) {
		super();
		this.dir = dir;
		this.index = index;
	}
	
	

}
