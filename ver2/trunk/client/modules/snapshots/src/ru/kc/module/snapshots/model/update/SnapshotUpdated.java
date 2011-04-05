package ru.kc.module.snapshots.model.update;

import ru.kc.module.snapshots.model.Snapshot;


public class SnapshotUpdated extends AbstractSnapshotsUpdate {
	
	public final Snapshot snapshot;

	public SnapshotUpdated(Snapshot snapshot) {
		super();
		this.snapshot = snapshot;
	}

}
