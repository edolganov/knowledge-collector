package ru.kc.module.snapshots.event;

import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.platform.domain.annotation.DomainSpecific;
import ru.kc.platform.event.Request;

@DomainSpecific
public class OpenSnapshotRequest extends Request<Void>{
	
	public final Snapshot snapshot;

	public OpenSnapshotRequest(Snapshot snapshot) {
		super();
		this.snapshot = snapshot;
	}
	

}
