package ru.kc.module.snapshots.tools;

import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.module.snapshots.command.MoveSnapshot;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class DragAndDropController extends Controller<SnapshotsPanel> implements TreeTransferHandler.Listener {
	
	TreeFacade treeFacade;
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		ui.tree.setTransferHandler(new TreeTransferHandler(this));
		ui.tree.setDragEnabled(true);
	}
	
	@Override
	public void onMove(DefaultMutableTreeNode destination, Object ob) {
		if(ob instanceof Snapshot){
			Snapshot snapshot = (Snapshot) ob;
			destination = getDirDestination(destination);
			if(destination != null){
				SnapshotDir dir = (SnapshotDir)destination.getUserObject();
				move(snapshot, dir);
			} else {
				log.info("unknow destination");
			}
		} else {
			log.info("unknow type to move: "+ob);
		}
	}

	private void move(Snapshot node, SnapshotDir parent) {
		invokeSafe(new MoveSnapshot(node, parent));
	}

	@Override
	public void onCopy(DefaultMutableTreeNode destination, Object ob) {
		destination = getDirDestination(destination);
		
		log.info("TODO copy");
	}
	
	private DefaultMutableTreeNode getDirDestination(DefaultMutableTreeNode destination) {
		if(destination == null) 
			return null;
		
		Object userObject = destination.getUserObject();
		if(userObject instanceof SnapshotDir){
			return destination;
		}
		else if(userObject instanceof Snapshot){
			return (DefaultMutableTreeNode) destination.getParent();
		}
		
		return null;
	}

}
