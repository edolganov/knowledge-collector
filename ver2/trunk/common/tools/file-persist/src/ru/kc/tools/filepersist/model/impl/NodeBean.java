package ru.kc.tools.filepersist.model.impl;

import java.util.ArrayList;
import java.util.List;

import ru.kc.model.Node;
import ru.kc.util.collection.Pair;

import com.thoughtworks.xstream.annotations.XStreamOmitField;


public abstract class NodeBean implements Node {
	
	protected String id;
	protected String name;
	protected String description;
	protected Long createDate;
	
	@XStreamOmitField
	protected Container container;
	protected List<String> childrenIds;
	
	
	public void addChildId(NodeBean node) {
		String childId = generateChildId(node);
		if(childrenIds == null) childrenIds = new ArrayList<String>();
		childrenIds.add(childId);
	}
	
	public void removeChildId(NodeBean node) {
		if(childrenIds == null) return;
		String childId = generateChildId(node);
		childrenIds.remove(childId);
	}
	
	private String generateChildId(NodeBean node){
		if(container == null) throw new IllegalStateException("parent must contains container: "+this);
		Container otherContainer = node.getContainer();
		if(otherContainer == null) throw new IllegalStateException("child must contains container: "+node);
		
		String childId = null;
		String nodeId = node.getId();
		if(container.equals(otherContainer)){
			childId = nodeId;
		} else {
			String filePath = otherContainer.getFileSimplePath();
			childId = filePath+":"+nodeId;
		}
		return childId;
	}
	
	public Pair<String, String> parse(String path) {
		String filePath = "";
		String childId = null;
		String[] split = path.split(":");
		int lastIndex = split.length-1;
		for (int i = 0; i < lastIndex; i++) {
			filePath = filePath + split[i];
		}
		childId = split[lastIndex];
		if(filePath.equals("")) filePath = container.getFileSimplePath();
		
		return new Pair<String, String>(filePath, childId);
	}

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
	public List<Node> getChildren() throws Exception {
		return container.getContext().tree.getChildren(this);
	}

	public List<String> getChildrenIds() {
		return childrenIds;
	}

	public void setChildrenIds(List<String> childrenIds) {
		this.childrenIds = childrenIds;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+" [id=" + id + ", name=" + name + ", description="
				+ description + ", createDate=" + createDate + "]";
	}






	

}
