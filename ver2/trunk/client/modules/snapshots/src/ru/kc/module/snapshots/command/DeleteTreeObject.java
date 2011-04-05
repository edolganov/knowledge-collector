package ru.kc.module.snapshots.command;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.command.Command;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.util.swing.tree.TreeFacade;

public class DeleteTreeObject extends Command<Void> {
	
	TreeFacade treeFacade;

	public DeleteTreeObject(JTree tree) {
		super();
		this.treeFacade = new TreeFacade(tree);
	}





	@Override
	protected Void invoke() throws Exception {
		Object ob = treeFacade.getCurrentObject();
		if(ob == null)
			return null;
		
		if(ob instanceof SnapshotDir){
			SnapshotDir dir = (SnapshotDir)ob;
			invoke(new DeleteSnapshotDir(dir));
		}
		else if(ob instanceof Snapshot){
			DefaultMutableTreeNode node = treeFacade.getCurrentNode();
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
			Snapshot snapshot = (Snapshot) node.getUserObject();
			SnapshotDir dir = (SnapshotDir)parent.getUserObject();
			invoke(new DeleteSnapshot(dir, snapshot));
		}
		
		return null;
	}

}
