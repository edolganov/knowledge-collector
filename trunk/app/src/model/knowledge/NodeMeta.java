package model.knowledge;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import model.knowledge.role.Parent;
import ru.chapaj.util.UuidGenerator;

public abstract class NodeMeta implements Parent{
	
	protected String uuid;
	protected String name;
	protected String description;
	protected ArrayList<Tag> tags;
	protected Long createDate;
	
	@XStreamOmitField
	private Root parent;

	public Root getParent() {
		return parent;
	}

	public void setParent(Root parent) {
		this.parent = parent;
	}


	public NodeMeta() {
		super();
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	public String getUuid() {
		if(uuid == null) {
			uuid = UuidGenerator.simpleUuid();
		}
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public String toString() {
		if(name == null) return "no name";
		return name;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	
	
	
}
