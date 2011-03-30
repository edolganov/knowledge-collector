package ru.kc.module.snapshots.command;

import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.SnapshotDirDeleted;
import ru.kc.module.snapshots.tools.SnapshotConverter;
import ru.kc.tools.filepersist.update.SetProperty;

public class DeleteSnapshotDir extends Command<Void> {
	
	SnapshotDir toDelete;
	

	public DeleteSnapshotDir(SnapshotDir toDelete) {
		this.toDelete = toDelete;
	}

	@Override
	protected Void invoke() throws Exception {
		Node owner = invoke(new GetOwner());
		
		boolean confirm = dialogs.confirmByDialog(rootUI, "Confirm the operation","Delete "+toDelete.getName()+"?");
		if(confirm){
			SnapshotConverter snapshotConverter = new SnapshotConverter();
			List<SnapshotDir> snapshotDirs = snapshotConverter.loadFrom(owner);
			snapshotDirs.remove(toDelete);
			SetProperty update = snapshotConverter.createUpdate(snapshotDirs, new SnapshotDirDeleted(toDelete));
			updater.update(owner, update);
		}
		return null;
	}

}
