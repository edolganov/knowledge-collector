package model.tree;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tree-snapshot")
public class TreeSnapshot {
	
	String name,data;
	
	

	public TreeSnapshot() {
		super();
	}

	public TreeSnapshot(String name, String data) {
		super();
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return name != null ? name : "noname snapshot";
	}

}
