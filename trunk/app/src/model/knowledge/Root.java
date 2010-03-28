package model.knowledge;

import java.util.ArrayList;
import java.util.List;

import ru.chapaj.util.UuidGenerator;

import model.HavingUuid;
import model.knowledge.role.Parent;
import model.tree.TreeSnapshotRoot;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;


@XStreamAlias("container")
public class Root implements Parent, HavingUuid {
	
	// [15.08.2009] jenua.dolganov: все дети лежат в перемешку
	List<RootElement> nodes;
	String uuid;
	
	@XStreamOmitField
	private String dirPath;
	

	public List<RootElement> getNodes() {
		if(nodes == null) nodes = new ArrayList<RootElement>();
		return nodes;
	}

	public void setNodes(ArrayList<RootElement> nodes) {
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

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	@Override
	public String getUuid() {
		if(uuid == null) {
			uuid = UuidGenerator.simpleUuid();
		}
		return uuid;
	}
	
	/**
	 * Получить id рута даже если он null
	 * @return
	 */
	@Deprecated
	public String getUnsafeUuid(){
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
