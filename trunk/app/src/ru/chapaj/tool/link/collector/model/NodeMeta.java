package ru.chapaj.tool.link.collector.model;

import java.util.ArrayList;

import model.HavingUuid;

import ru.chapaj.util.UuidGenerator;

public class NodeMeta implements HavingUuid {
	
	protected String uuid;
	protected String name;
	protected String description;
	protected ArrayList<Tag> tags;
	
	
	public NodeMeta() {
		super();
	}

	public NodeMeta(String name, String description, ArrayList<Tag> tags) {
		super();
		this.name = name;
		this.description = description;
		this.tags = tags;
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
	
	
	
}
