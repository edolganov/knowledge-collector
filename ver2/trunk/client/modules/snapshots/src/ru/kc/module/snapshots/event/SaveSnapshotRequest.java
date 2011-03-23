package ru.kc.module.snapshots.event;

import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.platform.domain.annotation.DomainSpecific;
import ru.kc.platform.event.Request;

@DomainSpecific
public class SaveSnapshotRequest extends Request<Void>{
	
	public final Snapshot snapshot;
	public final int snapshotDirIndex;
	
	public SaveSnapshotRequest(Snapshot snapshot, int snapshotDirIndex) {
		super();
		this.snapshot = snapshot;
		this.snapshotDirIndex = snapshotDirIndex;
	}
	

	
	
	

}
