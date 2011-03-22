package ru.kc.module.snapshots.model;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	
	private String id;
	private List<TreeNode> children = new ArrayList<TreeNode>();
	
	public void addChild(TreeNode child){
		children.add(child);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public List<TreeNode> getChildren() {
		return children;
	}
	
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", children=" + children + "]";
	}

}
