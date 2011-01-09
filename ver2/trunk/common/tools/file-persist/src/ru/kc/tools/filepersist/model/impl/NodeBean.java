package ru.kc.tools.filepersist.model.impl;

import java.util.Collection;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import ru.kc.model.Node;


public abstract class NodeBean implements Node {
	
	protected String id;
	protected String name;
	protected String description;
	protected Long createDate;
	
	@XStreamOmitField
	protected Container container;

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
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
		return null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+" [id=" + id + ", name=" + name + ", description="
				+ description + ", createDate=" + createDate + "]";
	}
	

}
