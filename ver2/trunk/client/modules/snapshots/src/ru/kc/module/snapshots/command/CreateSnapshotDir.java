package ru.kc.module.snapshots.command;

import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.SnapshotDirCreated;
import ru.kc.module.snapshots.tools.SnapshotConverter;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.util.UuidGenerator;

public class CreateSnapshotDir extends Command<Void> {
	
	String name;
	int index;
	Node owner;

	public CreateSnapshotDir(String name, int index) {
		super();
		this.name = name;
		this.index = index;
	}



	@Override
	protected Void invoke() throws Exception {
		
		owner = invoke(new GetOwner());
		
		SnapshotConverter snapshotConverter = new SnapshotConverter();
		List<SnapshotDir> list = snapshotConverter.loadFrom(owner);
		
		SnapshotDir newDir = new SnapshotDir();
		newDir.setId(UuidGenerator.simpleUuid());
		newDir.setName(name);
		list.add(index, newDir);
		
		SetProperty update = snapshotConverter.createUpdate(list, new SnapshotDirCreated(newDir, index));
		updater.update(owner, update);
		
		
		return null;
	}

}
