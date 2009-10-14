package model.knowledge;

import java.util.ArrayList;
import java.util.List;

import model.knowledge.role.Parent;
import model.tree.TreeSnapshotRoot;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;


@XStreamAlias("container")
public class Root implements Parent {
	
	// [15.08.2009] jenua.dolganov: все дети лежат в перемешку
	List<NodeMeta> nodes;
	
	//protected String uuid;
	@XStreamOmitField
	private String dirPath;
	

	public List<NodeMeta> getNodes() {
		if(nodes == null) nodes = new ArrayList<NodeMeta>();
		return nodes;
	}

	public void setNodes(ArrayList<NodeMeta> nodes) {
		this.nodes = nodes;
	}
	
	
	private TreeSnapshotRoot treeSnapshots;

	public TreeSnapshotRoot getTreeSnapshots() {
		if(treeSnapshots == null) treeSnapshots = new TreeSnapshotRoot();
		return treeSnapshots;
	}

	public void setTreeSnapshots(TreeSnapshotRoot treeSnapshots) {
		this.treeSnapshots = treeSnapshots;
	}
	
//	public String getUuid() {
//		if(uuid == null) {
//			uuid = UuidGenerator.simpleUuid();
//		}
//		return uuid;
//	}

//	public void setUuid(String uuid) {
//		this.uuid = uuid;
//	}

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

}
