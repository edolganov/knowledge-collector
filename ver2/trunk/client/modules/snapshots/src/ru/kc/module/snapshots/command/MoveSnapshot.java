package ru.kc.module.snapshots.command;

import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.SnapshotMovedToOtherDir;
import ru.kc.module.snapshots.tools.SnapshotDirConverter;
import ru.kc.tools.filepersist.update.SetProperty;

public class MoveSnapshot extends Command<Void>{
	
	Snapshot snapshot;
	SnapshotDir destination;

	public MoveSnapshot(Snapshot snapshot, SnapshotDir destination) {
		super();
		this.snapshot = snapshot;
		this.destination = destination;
	}


	@Override
	protected Void invoke() throws Exception {
		
		Node owner = invoke(new GetOwner());
		
		SnapshotDirConverter converter = new SnapshotDirConverter();
		List<SnapshotDir> list = converter.loadFrom(owner);
		
		SnapshotDir oldParent = findDir(list, snapshot);
		List<Snapshot> snapshots = oldParent.getSnapshots();
		Snapshot toMove = snapshots.get(snapshots.indexOf(snapshot));
		SnapshotDir newParent = list.get(list.indexOf(destination));
		
		if(newParent.equals(oldParent)) 
			return null;
		
		oldParent.getSnapshots().remove(toMove);
		newParent.getSnapshots().add(toMove);
		
		SetProperty update = converter.createUpdate(list, new SnapshotMovedToOtherDir(oldParent, toMove, newParent));
		updater.update(owner, update);
		
		return null;
	}


	private SnapshotDir findDir(List<SnapshotDir> list, Snapshot snapshot) {
		for(SnapshotDir dir : list){
			int index = dir.getSnapshots().indexOf(snapshot);
			if(index > -1){
				return dir;
			}
		}
		throw new IllegalStateException("can't find "+snapshot+" in "+list);
	}

}
