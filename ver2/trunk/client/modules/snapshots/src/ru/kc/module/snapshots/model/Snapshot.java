package ru.kc.module.snapshots.model;


public class Snapshot  {
	
	private String id;
	private String name;
	private TreeNode root;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	@Override
	public String toString() {
		return "Snapshot [id=" + id + ", name=" + name + "]";
	}

	
}
