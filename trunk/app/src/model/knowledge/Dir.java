package model.knowledge;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("dir")
public class Dir extends NodeMeta {
	
	// [15.08.2009] jenua.dolganov: все дети лежат в перемешку
	ArrayList<NodeMeta> nodes;

	public ArrayList<NodeMeta> getNodes() {
		if(nodes == null) nodes = new ArrayList<NodeMeta>();
		return nodes;
	}

	public void setNodes(ArrayList<NodeMeta> nodes) {
		this.nodes = nodes;
	}
	
	

}
