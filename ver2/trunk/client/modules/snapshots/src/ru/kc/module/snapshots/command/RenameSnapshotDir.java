package ru.kc.module.snapshots.command;

import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.SnapshotDirRenamed;
import ru.kc.module.snapshots.tools.SnapshotDirConverter;
import ru.kc.tools.filepersist.update.SetProperty;

public class RenameSnapshotDir extends Command<Void> {
	
	SnapshotDir dir;
	String newName;
	

	

	public RenameSnapshotDir(SnapshotDir dir, String newName) {
		super();
		this.dir = dir;
		this.newName = newName;
	}


	@Override
	protected Void invoke() throws Exception {
		Node owner = invoke(new GetOwner());
		
		SnapshotDirConverter snapshotConverter = new SnapshotDirConverter();
		List<SnapshotDir> snapshotDirs = snapshotConverter.loadFrom(owner);
		SnapshotDir toRename = snapshotDirs.get(snapshotDirs.indexOf(dir));
		toRename.setName(newName);
		SetProperty update = snapshotConverter.createUpdate(snapshotDirs, new SnapshotDirRenamed(toRename));
		updater.update(owner, update);
		
		return null;
	}

}
