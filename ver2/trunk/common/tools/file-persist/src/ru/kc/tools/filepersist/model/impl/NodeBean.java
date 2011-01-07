package ru.kc.tools.filepersist.model.impl;

import java.util.Collection;

import ru.kc.model.Node;


public abstract class NodeBean implements Node {
	
	protected String id;
	protected String name;
	protected String description;
	protected Long createDate;
	protected Collection<Node> children;
	protected NodeBean parent;
	
	

	public NodeBean getParent() {
		return parent;
	}

	public void setParent(NodeBean parent) {
		this.parent = parent;
	}

	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public Long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	
	@Override
	public Collection<Node> getChildren() {
		return children;
	}
	public void setChildren(Collection<Node> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "NodeBean [id=" + id + ", name=" + name + ", description="
				+ description + ", createDate=" + createDate + "]";
	}
	

}
