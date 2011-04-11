package ru.kc.module.snapshots.command;

import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.TreeNode;
import ru.kc.module.snapshots.model.update.SnapshotUpdated;
import ru.kc.module.snapshots.tools.SnapshotDirConverter;
import ru.kc.tools.filepersist.update.SetProperty;

public class UpdateSnapshot extends Command<Void> {
	
	SnapshotDir parent;
	Snapshot toUpdate;
	

	public UpdateSnapshot(SnapshotDir parent, Snapshot toUpdate) {
		super();
		this.parent = parent;
		this.toUpdate = toUpdate;
	}



	@Override
	protected Void invoke() throws Exception {
		Node owner = invoke(new GetOwner());
		TreeNode treeNode = invoke(new CreateTreeNodes());
		
		SnapshotDirConverter snapshotConverter = new SnapshotDirConverter();
		List<SnapshotDir> list = snapshotConverter.loadFrom(owner);
		int dirIndex = list.indexOf(parent);
		SnapshotDir dir = list.get(dirIndex);
		List<Snapshot> snapshots = dir.getSnapshots();
		Snapshot snapshot = snapshots.get(snapshots.indexOf(toUpdate));
		
		snapshot.setRoot(treeNode);
		SetProperty update = snapshotConverter.createUpdate(list, new SnapshotUpdated(snapshot));
		updater.update(owner, update);
		
		return null;
	}

}
