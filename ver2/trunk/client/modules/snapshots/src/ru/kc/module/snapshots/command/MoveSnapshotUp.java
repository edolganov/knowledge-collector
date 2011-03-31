package ru.kc.module.snapshots.command;

import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.SnapshotMoved;
import ru.kc.module.snapshots.tools.SnapshotConverter;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.util.collection.ListUtil;

public class MoveSnapshotUp extends Command<Void>{
	
	SnapshotDir dir;
	Snapshot snapshot;
	
	public MoveSnapshotUp(SnapshotDir dir, Snapshot snapshot) {
		super();
		this.dir = dir;
		this.snapshot = snapshot;
	}





	@Override
	protected Void invoke() throws Exception {
		
		Node owner = invoke(new GetOwner());
		
		SnapshotConverter converter = new SnapshotConverter();
		List<SnapshotDir> list = converter.loadFrom(owner);
		SnapshotDir parent = list.get(list.indexOf(dir));
		List<Snapshot> snapshots = parent.getSnapshots();
		Snapshot toMove = snapshots.get(snapshots.indexOf(snapshot));
		
		int newIndex = ListUtil.moveUp(snapshots, toMove);
		
		SetProperty update = converter.createUpdate(list, new SnapshotMoved(parent, toMove, newIndex));
		updater.update(owner, update);
		
		return null;
	}

}
