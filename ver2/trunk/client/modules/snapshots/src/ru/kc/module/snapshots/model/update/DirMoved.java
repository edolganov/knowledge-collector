package ru.kc.module.snapshots.model.update;

import ru.kc.module.snapshots.model.SnapshotDir;


public class DirMoved extends AbstractSnapshotsUpdate {
	
	public final SnapshotDir dir;
	public final int newIndex;
	
	public DirMoved(SnapshotDir dir, int newIndex) {
		super();
		this.dir = dir;
		this.newIndex = newIndex;
	}
	
	
	
	

}
