package ru.kc.tools.filepersist.model.impl;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ru.kc.model.Snapshot;
import ru.kc.model.SnapshotDir;

@XStreamAlias("snapshot-dir")
public class SnapshotDirBean implements SnapshotDir {
	
	
	private boolean open;
	private String name;
	private List<SnapshotBean> snapshots;
	

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

	@Override
	public boolean isOpen() {
		return open;
	}
	
	public void setSnapshots(List<SnapshotBean> snapshots) {
		this.snapshots = snapshots;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Snapshot> getSnapshots() {
		if(snapshots == null)
			return new ArrayList<Snapshot>(0);
		return (List)snapshots;
	}
}
