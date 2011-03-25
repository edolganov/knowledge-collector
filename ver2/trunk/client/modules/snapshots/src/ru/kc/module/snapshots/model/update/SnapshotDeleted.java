package ru.kc.module.snapshots.model.update;

import ru.kc.module.snapshots.model.Snapshot;


public class SnapshotDeleted extends SnapshotsUpdate {
	
	public final Snapshot snapshot;

	public SnapshotDeleted(Snapshot snapshot) {
		super();
		this.snapshot = snapshot;
	}

}
