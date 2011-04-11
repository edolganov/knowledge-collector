package ru.kc.module.snapshots.command;

import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.SnapshotDeleted;
import ru.kc.module.snapshots.tools.SnapshotDirConverter;
import ru.kc.tools.filepersist.update.SetProperty;

public class DeleteSnapshot extends Command<Void> {
	
	SnapshotDir parent;
	Snapshot toDelete;
	

	public DeleteSnapshot(SnapshotDir parent, Snapshot toDelete) {
		super();
		this.parent = parent;
		this.toDelete = toDelete;
	}



	@Override
	protected Void invoke() throws Exception {
		Node owner = invoke(new GetOwner());
		
		boolean confirm = dialogs.confirmByDialog(rootUI, "Confirm the operation","Delete "+toDelete.getName()+"?");
		if(confirm){
			SnapshotDirConverter snapshotConverter = new SnapshotDirConverter();
			List<SnapshotDir> list = snapshotConverter.loadFrom(owner);
			int dirIndex = list.indexOf(parent);
			SnapshotDir dir = list.get(dirIndex);
			dir.getSnapshots().remove(toDelete);
			
			SetProperty update = snapshotConverter.createUpdate(list, new SnapshotDeleted(toDelete));
			updater.update(owner, update);
		}
		return null;
	}

}
