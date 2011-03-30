package ru.kc.module.snapshots.command;

import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.TreeNode;
import ru.kc.module.snapshots.model.update.SnapshotCreated;
import ru.kc.module.snapshots.tools.SnapshotConverter;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.util.UuidGenerator;

public class CreateSnapshot extends Command<Void> {
	
	String name;
	int dirIndex;
	Node owner;

	public CreateSnapshot(String name, int dirIndex) {
		super();
		this.name = name;
		this.dirIndex = dirIndex;
	}



	@Override
	protected Void invoke() throws Exception {
		
		owner = invoke(new GetOwner());
		
		TreeNode root = invokeSafe(new CreateTreeNodes()).result;
		if(root == null)
			throw new IllegalStateException("TreeNode root is null");
		
		SnapshotConverter snapshotConverter = new SnapshotConverter();
		List<SnapshotDir> list = snapshotConverter.loadFrom(owner);
		SnapshotDir parentDir = list.get(dirIndex);
		
		Snapshot snapshot = new Snapshot();
		snapshot.setId(UuidGenerator.simpleUuid());
		snapshot.setName(name);
		snapshot.setRoot(root);
		parentDir.add(snapshot);
		
		SetProperty update = snapshotConverter.createUpdate(list, new SnapshotCreated(parentDir, snapshot));
		updater.update(owner, update);
		
		
		return null;
	}

}
