package ru.kc.module.snapshots.model;

import java.util.ArrayList;
import java.util.List;

public class Snapshot  {
	
	
	private List<String> leafSubNodesIds;
	

	public Snapshot() {}

	public Snapshot(String... ids) {
		super();
		leafSubNodesIds = new ArrayList<String>();
		if(ids != null){
			for(String id : ids){
				leafSubNodesIds.add(id);
			}
		}
	}

	public List<String> getLeafSubNodesIds() {
		return leafSubNodesIds;
	}

	public void setLeafSubNodesIds(List<String> leafSubNodesIds) {
		this.leafSubNodesIds = leafSubNodesIds;
	}

	public String listToString(){
		if(leafSubNodesIds == null || leafSubNodesIds.size() == 0)
			return "";
		else {
			StringBuilder sb = new StringBuilder();
			int last = leafSubNodesIds.size()-1;
			for(int i=0; i < last; i++){
				sb.append(leafSubNodesIds.get(i)).append(" ");
			}
			sb.append(leafSubNodesIds.get(last));
			return sb.toString();
		}
	}
}
