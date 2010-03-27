package model.knowledge;

import java.util.ArrayList;

/**
 * Нода
 * @author jenua.dolganov
 *
 */
public abstract class Node extends RootElement {
	
	protected String name;
	protected String description;
	protected ArrayList<Tag> tags;
	
	public Node() {
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

	@Override
	public String toString() {
		if(name == null) return "no name";
		return name;
	}
	
	
	
}
