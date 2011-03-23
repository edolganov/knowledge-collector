package ru.kc.module.snapshots.event;

import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.platform.domain.annotation.DomainSpecific;
import ru.kc.platform.event.Request;

@DomainSpecific
public class SaveSnapshotDirRequest extends Request<Void>{
	
	public final SnapshotDir newDir;
	public final int index;
	
	public SaveSnapshotDirRequest(SnapshotDir newDir, int index) {
		super();
		this.newDir = newDir;
		this.index = index;
	}
	

}
