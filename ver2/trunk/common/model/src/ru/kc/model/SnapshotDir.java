package ru.kc.model;

import java.util.List;

public interface SnapshotDir {
	
	String getName();
	
	List<Snapshot> getSnapshots();
	
	boolean isOpen();

}
