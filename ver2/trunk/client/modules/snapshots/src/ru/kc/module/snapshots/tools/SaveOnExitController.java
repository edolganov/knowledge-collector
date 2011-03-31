package ru.kc.module.snapshots.tools;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.model.Node;
import ru.kc.module.snapshots.command.GetOwner;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.common.event.AppClosing;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class SaveOnExitController extends Controller<SnapshotsPanel>{

	private TreeFacade treeFacade;
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
	}
	
	@EventListener
	public void onClosing(AppClosing event){
		saveSnapshots();
	}
	
	private void saveSnapshots() {
		Node owner = invokeSafe(new GetOwner()).result;
		if(owner == null)
			return;
		
		List<SnapshotDir> snapshotDirs = getModel();
		SetProperty update = new SnapshotConverter().createUpdate(snapshotDirs, null);
		try {
			updater.update(owner, update);
		}catch (Exception e) {
			log.error("", e);
		}
		
	}

	private List<SnapshotDir> getModel() {
		ArrayList<SnapshotDir> out = new ArrayList<SnapshotDir>();
		DefaultMutableTreeNode root = treeFacade.getRoot();
		for(DefaultMutableTreeNode treeNode : treeFacade.getChildren(root)){
			SnapshotDir dir = (SnapshotDir)treeNode.getUserObject();
			boolean expanded = treeFacade.isExpanded(treeNode);
			dir.setOpen(expanded);
			
			ArrayList<Snapshot> snapshots = new ArrayList<Snapshot>();
			for(DefaultMutableTreeNode snapshotNode : treeFacade.getChildren(treeNode)){
				Snapshot snapshot = (Snapshot)snapshotNode.getUserObject();
				snapshots.add(snapshot);
			}
			dir.setSnapshots(snapshots);

			out.add(dir);
		}
		return out;
	}
	

}
