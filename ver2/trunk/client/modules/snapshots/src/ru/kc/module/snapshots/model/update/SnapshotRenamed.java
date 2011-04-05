package ru.kc.module.snapshots.model.update;

import ru.kc.module.snapshots.model.Snapshot;


public class SnapshotRenamed extends SnapshotsUpdate {
	
	public final Snapshot snapshot;

	public SnapshotRenamed(Snapshot snapshot) {
		super();
		this.snapshot = snapshot;
	}

}
