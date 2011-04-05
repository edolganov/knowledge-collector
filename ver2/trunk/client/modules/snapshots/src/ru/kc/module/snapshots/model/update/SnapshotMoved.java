package ru.kc.module.snapshots.model.update;

import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;


public class SnapshotMoved extends AbstractSnapshotsUpdate {
	
	public final SnapshotDir dir;
	public final Snapshot snapshot;
	public final int newIndex;
	
	
	public SnapshotMoved(SnapshotDir dir, Snapshot snapshot, int newIndex) {
		super();
		this.dir = dir;
		this.snapshot = snapshot;
		this.newIndex = newIndex;
	}
	

}
