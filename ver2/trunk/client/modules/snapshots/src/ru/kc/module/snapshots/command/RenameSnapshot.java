package ru.kc.module.snapshots.command;

import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.SnapshotRenamed;
import ru.kc.module.snapshots.tools.SnapshotDirConverter;
import ru.kc.tools.filepersist.update.SetProperty;

public class RenameSnapshot extends Command<Void> {
	
	SnapshotDir parent;
	Snapshot snapshot;
	String newName;
	

	public RenameSnapshot(SnapshotDir parent, Snapshot snapshot, String newName) {
		super();
		this.parent = parent;
		this.snapshot = snapshot;
		this.newName = newName;
	}






	@Override
	protected Void invoke() throws Exception {
		Node owner = invoke(new GetOwner());
		
		SnapshotDirConverter snapshotConverter = new SnapshotDirConverter();
		List<SnapshotDir> list = snapshotConverter.loadFrom(owner);
		int dirIndex = list.indexOf(parent);
		SnapshotDir dir = list.get(dirIndex);
		List<Snapshot> snapshots = dir.getSnapshots();
		Snapshot toRename = snapshots.get(snapshots.indexOf(snapshot));
		
		toRename.setName(newName);
		
		SetProperty update = snapshotConverter.createUpdate(list, new SnapshotRenamed(toRename));
		updater.update(owner, update);
		
		return null;
	}

}
