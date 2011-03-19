package ru.kc.tools.filepersist.model.impl;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import ru.kc.model.Node;
import ru.kc.model.Snapshot;

@XStreamAlias("snapshot")
public class SnapshotBean implements Snapshot {
	
	
	private List<String> leafSubNodesIds;
	
	@XStreamOmitField
	private NodeBean owner;

	public void setOwner(NodeBean owner) {
		this.owner = owner;
	}

	public List<String> getLeafSubNodesIds() {
		return leafSubNodesIds;
	}

	public void setLeafSubNodesIds(List<String> leafSubNodesIds) {
		this.leafSubNodesIds = leafSubNodesIds;
	}

	@Override
	public List<Node> getLeafSubNodes() throws Exception {
		Container container = owner.getContainer();
		return container.getContext().snapshotService.getLeafSubNodes(owner, this);
	}

}
