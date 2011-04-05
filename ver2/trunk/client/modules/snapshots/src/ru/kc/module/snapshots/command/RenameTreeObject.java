package ru.kc.module.snapshots.command;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.command.Command;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.util.swing.tree.TreeFacade;

public class RenameTreeObject extends Command<Void> {
	
	TreeFacade treeFacade;
	String newName;

	public RenameTreeObject(JTree tree, String newName) {
		super();
		this.treeFacade = new TreeFacade(tree);
		this.newName = newName;
	}





	@Override
	protected Void invoke() throws Exception {
		Object ob = treeFacade.getCurrentObject();
		if(ob == null)
			return null;
		
		if(ob instanceof SnapshotDir){
			SnapshotDir dir = (SnapshotDir)ob;
			invoke(new RenameSnapshotDir(dir, newName));
		}
		else if(ob instanceof Snapshot){
			DefaultMutableTreeNode node = treeFacade.getCurrentNode();
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
			Snapshot snapshot = (Snapshot) node.getUserObject();
			SnapshotDir dir = (SnapshotDir)parent.getUserObject();
			invoke(new RenameSnapshot(dir, snapshot, newName));
		}
		
		return null;
	}

}
