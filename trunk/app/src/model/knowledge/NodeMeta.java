package model.knowledge;

import java.util.ArrayList;

import ru.chapaj.util.UuidGenerator;

public abstract class NodeMeta {
	
	protected String uuid;
	protected String name;
	protected String description;
	protected ArrayList<Tag> tags;
	protected long createDate;
	
	
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

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
	
	
}
