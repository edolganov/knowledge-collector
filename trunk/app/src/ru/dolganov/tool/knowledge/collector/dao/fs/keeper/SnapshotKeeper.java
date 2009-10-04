package ru.dolganov.tool.knowledge.collector.dao.fs.keeper;

import java.util.List;
import java.util.Map;

import model.knowledge.Root;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;

public class SnapshotKeeper extends AbstractKeeper {
	
	private static final String LAST_TREE_STATE = "lastTreeState";
	private static final String SNAPSHOT = "snapshot";


	public boolean persist(Root root, TreeSnapshot treeSnapshot, Map<String, Object> params){
		if(params == null) return false;
		if(params.containsKey(LAST_TREE_STATE)){
			root.getTreeSnapshots().setLastTreeState(treeSnapshot);
		}
		else if(params.containsKey(SNAPSHOT)){
			String dirName = (String)params.get(SNAPSHOT);
			if(dirName == null) return false;
			getDir(root, dirName).getSnapshots().add(treeSnapshot);
		}
		else return false;
		
		return true;
	}
	
	public TreeSnapshotDir getSnapDir(Root root,Map<String, Object> params){
		TreeSnapshotDir out = null;
		if(params.containsKey(SNAPSHOT)){
			String dirName = (String)params.get(SNAPSHOT);
			if(dirName != null){
				out = getDir(root, dirName, false);
			}
		}
		return out;
	}
	
	public TreeSnapshotDir persist(Root root, String dirName){
		TreeSnapshotDir dir = getDir(root, dirName,false);
		if(dir != null) return null;
		dir = create(root, dirName);
		return dir;
	}

	private TreeSnapshotDir getDir(Root root, String dirName) {
		return getDir(root, dirName, true);
	}
	
	private TreeSnapshotDir getDir(Root root, String dirName, boolean create) {
		TreeSnapshotDir dir = null;
		List<TreeSnapshotDir> dirs = root.getTreeSnapshots().getSnaphotDirs();
		for(TreeSnapshotDir d : dirs){
			if(d.getName().equals(dirName)){
				dir = d;
				break;
			}
		}
		if(dir == null && create){
			dir = create(root, dirName);
		}
		return dir;
	}
	
	private TreeSnapshotDir create(Root root, String dirName){
		TreeSnapshotDir dir = new TreeSnapshotDir();
		dir.setName(dirName);
		List<TreeSnapshotDir> dirs = root.getTreeSnapshots().getSnaphotDirs();
		dirs.add(dir);;
		return dir;
	}
	
	

}
