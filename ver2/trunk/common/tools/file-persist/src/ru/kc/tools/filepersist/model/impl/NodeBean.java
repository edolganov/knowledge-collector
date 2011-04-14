package ru.kc.tools.filepersist.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.kc.model.Node;
import ru.kc.util.collection.ListUtil;
import ru.kc.util.collection.Pair;

import com.thoughtworks.xstream.annotations.XStreamOmitField;


public abstract class NodeBean implements Node, Cloneable {
	
	protected String id;
	protected String name;
	protected String description;
	protected Long createDate;
	protected String parentId;
	protected List<String> childrenIds;
	protected Map<String, String> properties;
	
	@XStreamOmitField
	protected Container container;
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public void setParent(NodeBean parent) {
		setParentId(generateNodeIdWithContainerPath(parent));
	}
	
	public void removeParent(){
		setParentId(null);
	}

	public void addChild(NodeBean node) {
		String childId = generateNodeIdWithContainerPath(node);
		if(childrenIds == null) childrenIds = new ArrayList<String>();
		childrenIds.add(childId);
	}
	
	public void removeChild(NodeBean node) {
		String childId = generateNodeIdWithContainerPath(node);
		boolean deleted = childrenIds.remove(childId);
		if(!deleted) throw new IllegalArgumentException("can't find child "+node+" in parent "+this);
		
	}
	
	public int moveChildUp(NodeBean node) {
		if(childrenIds == null) return -1;
		String childId = generateNodeIdWithContainerPath(node);
		int newIndex = ListUtil.moveUp(childrenIds, childId);;
		return newIndex;
	}
	
	public int moveChildDown(NodeBean node) {
		if(childrenIds == null) return -1;
		String childId = generateNodeIdWithContainerPath(node);
		int newIndex = ListUtil.moveDown(childrenIds, childId);
		return newIndex;
	}
	
	private String generateNodeIdWithContainerPath(NodeBean node){
		if(container == null) throw new IllegalStateException(this+" must contains container");
		Container otherContainer = node.getContainer();
		if(otherContainer == null) throw new IllegalStateException(node+" must contains container");
		
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
	
	public List<String> getChildrenIds() {
		if(childrenIds == null) return new ArrayList<String>(0);
		return childrenIds;
	}

	public void setChildrenIds(List<String> childrenIds) {
		this.childrenIds = childrenIds;
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
		if(filePath.equals("")) 
			filePath = container.getFileSimplePath();
		
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
	
	public Node getParent() throws Exception {
		return container.getContext().tree.getParent(this);
	}
	
	public String getProperty(String key){
		if(properties == null) 
			return null;
		return properties.get(key);
	}
	
	public void setProperty(String key, String value){
		if(properties == null) 
			properties = new HashMap<String, String>();
		properties.put(key, value);
	}
	
	public void removeProperty(String key){
		if(properties == null) 
			return;
		properties.remove(key);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+" [id=" + id + ", name=" + name + ", description="
				+ description + ", createDate=" + createDate + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeBean other = (NodeBean) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}




	

}
