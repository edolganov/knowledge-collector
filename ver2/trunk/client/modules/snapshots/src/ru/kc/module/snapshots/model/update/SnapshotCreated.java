package ru.kc.module.snapshots.model.update;

import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;


public class SnapshotCreated extends AbstractSnapshotsUpdate {
	
	public final SnapshotDir parentDir;
	public final Snapshot snapshot;
	
	public SnapshotCreated(SnapshotDir parentDir, Snapshot snapshot) {
		super();
		this.parentDir = parentDir;
		this.snapshot = snapshot;
	}

}
