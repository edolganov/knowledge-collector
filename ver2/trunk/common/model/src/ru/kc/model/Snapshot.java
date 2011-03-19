package ru.kc.model;

import java.util.List;

public interface Snapshot {
	
	List<Node> getLeafSubNodes() throws Exception;

}
