package ru.kc.module.snapshots;

import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.GlobalMapping;
import ru.kc.platform.module.Module;

@GlobalMapping("snapshots")
public class SnapshotsModule extends Module<SnapshotsPanel>{

	@Override
	protected SnapshotsPanel createUI() {
		return new SnapshotsPanel();
	}

}
