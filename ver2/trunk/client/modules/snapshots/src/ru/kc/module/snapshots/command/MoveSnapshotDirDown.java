package ru.kc.module.snapshots.command;

import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.DirMoved;
import ru.kc.module.snapshots.tools.SnapshotDirConverter;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.util.collection.ListUtil;

public class MoveSnapshotDirDown extends Command<Void>{
	
	SnapshotDir dir;

	public MoveSnapshotDirDown(SnapshotDir dir) {
		super();
		this.dir = dir;
	}



	@Override
	protected Void invoke() throws Exception {
		
		Node owner = invoke(new GetOwner());
		
		SnapshotDirConverter converter = new SnapshotDirConverter();
		List<SnapshotDir> list = converter.loadFrom(owner);
		int index = list.indexOf(dir);
		SnapshotDir toMove = list.get(index);
		int newIndex = ListUtil.moveDown(list, toMove);
		
		SetProperty update = converter.createUpdate(list, new DirMoved(toMove, newIndex));
		updater.update(owner, update);
		
		return null;
	}

}
