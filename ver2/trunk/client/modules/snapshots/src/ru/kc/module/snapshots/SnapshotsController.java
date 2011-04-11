package ru.kc.module.snapshots;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import ru.kc.common.controller.Controller;
import ru.kc.model.Node;
import ru.kc.module.snapshots.command.GetOwner;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.DirMoved;
import ru.kc.module.snapshots.model.update.SnapshotCreated;
import ru.kc.module.snapshots.model.update.SnapshotDeleted;
import ru.kc.module.snapshots.model.update.SnapshotDirCreated;
import ru.kc.module.snapshots.model.update.SnapshotDirDeleted;
import ru.kc.module.snapshots.model.update.SnapshotDirRenamed;
import ru.kc.module.snapshots.model.update.SnapshotMoved;
import ru.kc.module.snapshots.model.update.SnapshotMovedToOtherDir;
import ru.kc.module.snapshots.model.update.SnapshotRenamed;
import ru.kc.module.snapshots.model.update.AbstractSnapshotsUpdate;
import ru.kc.module.snapshots.model.update.SnapshotUpdated;
import ru.kc.module.snapshots.tools.CellRender;
import ru.kc.module.snapshots.tools.SnapshotDirConverter;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class SnapshotsController extends Controller<SnapshotsPanel>{
	
	private TreeFacade treeFacade;
	private SnapshotDirConverter snapshotConverter = new SnapshotDirConverter();
	
	@Override
	protected void init() {
		ui.remove.setToolTipText("Delete  (Delete)");
		ui.up.setToolTipText("Move up  (Ctrl+UP)");
		ui.down.setToolTipText("Move down  (Ctrl+DOWN)");
		
		treeFacade = new TreeFacade(ui.tree);
		ui.tree.setModel(TreeFacade.createModelByUserObject(""));
		ui.tree.setRootVisible(false);
		ui.tree.setCellRenderer(new CellRender());
		treeFacade.setSingleSelection();

		buildTree();
	}


	private void buildTree() {
		treeFacade.clear();
		
		Node owner = invokeSafe(new GetOwner()).result;
		if(owner == null) return;
		

		List<SnapshotDir> snaphots = snapshotConverter.loadFrom(owner);
		DefaultMutableTreeNode treeRoot = treeFacade.getRoot();
		for(SnapshotDir dir : snaphots){
			DefaultMutableTreeNode dirNode = treeFacade.addChild(treeRoot, dir);
			for(Snapshot snapshot : dir.getSnapshots()){
				treeFacade.addChild(dirNode, snapshot);
			}
		}
		
		for(DefaultMutableTreeNode child : treeFacade.getChildren(treeRoot)){
			SnapshotDir dir = (SnapshotDir)child.getUserObject();
			if(dir.isOpen()){
				treeFacade.expand(child);
			}
		}
	}



	@Override
	protected void onNodeUpdated(Node old, Node updatedNode, Collection<UpdateRequest> nodeUpdates) {
		Node owner = invokeSafe(new GetOwner()).result;
		if(owner == null) return;
		
		if(old.equals(owner)){
			
			List<AbstractSnapshotsUpdate> updates = findUpdates(nodeUpdates);
			if(updates.size() > 0){
				synchronizedTree(updates);
			}

		}
	}

	private List<AbstractSnapshotsUpdate> findUpdates(Collection<UpdateRequest> nodeUpdates) {
		ArrayList<AbstractSnapshotsUpdate> out = new ArrayList<AbstractSnapshotsUpdate>();
		for(UpdateRequest nodeUpdate : nodeUpdates){
			if(nodeUpdate instanceof SetProperty){
				Object additionInfo = ((SetProperty) nodeUpdate).additionInfo;
				if(additionInfo instanceof AbstractSnapshotsUpdate){
					out.add((AbstractSnapshotsUpdate)additionInfo);
				}
			}
		}
		return out;
	}

	private void synchronizedTree(List<AbstractSnapshotsUpdate> updates) {
		for(AbstractSnapshotsUpdate update : updates){
			synchronizedTree(update);
		}
	}

	private void synchronizedTree(AbstractSnapshotsUpdate update) {
		if(update instanceof SnapshotCreated){
			process((SnapshotCreated)update);
		}
		else if(update instanceof SnapshotDeleted){
			process((SnapshotDeleted)update);
		}
		else if(update instanceof SnapshotDirCreated){
			process((SnapshotDirCreated)update);
		}
		else if(update instanceof SnapshotDirDeleted){
			process((SnapshotDirDeleted)update);
		}
		else if(update instanceof DirMoved){
			process((DirMoved)update);
		}
		else if(update instanceof SnapshotMoved){
			process((SnapshotMoved)update);
		}
		else if(update instanceof SnapshotMovedToOtherDir){
			process((SnapshotMovedToOtherDir)update);
		}
		else if(update instanceof SnapshotDirRenamed){
			process((SnapshotDirRenamed)update);
		}
		else if(update instanceof SnapshotRenamed){
			process((SnapshotRenamed)update);
		}
		else if(update instanceof SnapshotUpdated){
			process((SnapshotUpdated)update);
		}
	}
	
	private void process(SnapshotCreated update){
		SnapshotDir parentDir = update.parentDir;
		Snapshot snapshot = update.snapshot;
		DefaultMutableTreeNode parentNode = findNode(parentDir.getId());
		DefaultMutableTreeNode childNode = treeFacade.addChild(parentNode, snapshot);
		treeFacade.setSelection(childNode);
	}
	


	private void process(SnapshotDeleted update){
		Snapshot snapshot = update.snapshot;
		DefaultMutableTreeNode node = findNode(snapshot.getId());
		treeFacade.removeNode(node);
	}
	
	private void process(SnapshotDirCreated update){
		SnapshotDir dir = update.dir;
		int index = update.index;
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(dir);
		DefaultMutableTreeNode root = treeFacade.getRoot();
		List<TreePath> openPaths = getOpenPaths(root);
		root.insert(node, index);
		treeFacade.getModel().reload(root);
		treeFacade.setSelection(node);
		for(TreePath path : openPaths){
			treeFacade.tree.expandPath(path);
		}
	}
	
	private List<TreePath> getOpenPaths(DefaultMutableTreeNode root) {
		ArrayList<TreePath> out = new ArrayList<TreePath>();
		for(int i=0; i < root.getChildCount(); ++i){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i);
			if(treeFacade.isExpanded(node)){
				out.add(new TreePath(node.getPath()));
			}
		}
		return out;
	}

	private void process(SnapshotDirDeleted update){
		SnapshotDir dir = update.dir;
		DefaultMutableTreeNode node = findNode(dir.getId());
		treeFacade.removeNode(node);
	}
	
	private void process(SnapshotDirRenamed update){
		SnapshotDir renamed = update.dir;
		DefaultMutableTreeNode node = findNode(renamed.getId());
		SnapshotDir dir = (SnapshotDir)node.getUserObject();
		dir.setName(renamed.getName());
		treeFacade.refresh(node);
	}
	
	private void process(SnapshotRenamed update){
		Snapshot renamed = update.snapshot;
		DefaultMutableTreeNode node = findNode(renamed.getId());
		Snapshot snapshot = (Snapshot)node.getUserObject();
		snapshot.setName(renamed.getName());
		treeFacade.refresh(node);
	}
	
	private void process(SnapshotUpdated update){
		Snapshot updated = update.snapshot;
		DefaultMutableTreeNode node = findNode(updated.getId());
		Snapshot snapshot = (Snapshot)node.getUserObject();
		snapshot.setRoot(updated.getRoot());
		treeFacade.refresh(node);
	}
	
	private void process(SnapshotMovedToOtherDir update){
		Snapshot snapshot = update.snapshot;
		SnapshotDir newDir = update.newDir;
		
		DefaultMutableTreeNode node = findNode(snapshot.getId());
		DefaultMutableTreeNode newParent = findNode(newDir.getId());
		treeFacade.moveNode(newParent, node);
		openDirs();
	}
	
	private void process(DirMoved update){
		SnapshotDir dir = update.dir;
		int newIndex = update.newIndex;
		DefaultMutableTreeNode node = findNode(dir.getId());
		treeFacade.moveChild(node, newIndex);
		openDirs();
	}


	private void process(SnapshotMoved update){
		Snapshot snapshot = update.snapshot;
		int newIndex = update.newIndex;
		DefaultMutableTreeNode node = findNode(snapshot.getId());
		treeFacade.moveChild(node, newIndex);
		openDirs();
	}
	
	private void openDirs() {
		DefaultMutableTreeNode root = treeFacade.getRoot();
		for(int i=0; i < root.getChildCount(); ++i){
			DefaultMutableTreeNode dirNode = (DefaultMutableTreeNode)root.getChildAt(i);
			SnapshotDir dir = (SnapshotDir) dirNode.getUserObject();
			boolean open = dir.isOpen();
			if(open)
				treeFacade.expand(dirNode);
		}
	}
	

	private DefaultMutableTreeNode findNode(String id) {
		DefaultMutableTreeNode root = treeFacade.getRoot();
		for(int i=0; i < root.getChildCount(); ++i){
			DefaultMutableTreeNode dirNode = (DefaultMutableTreeNode)root.getChildAt(i);
			SnapshotDir dir = (SnapshotDir) dirNode.getUserObject();
			if(dir.getId().equals(id)){
				return dirNode;
			}
			
			for(int j=0; j < dirNode.getChildCount(); ++j){
				DefaultMutableTreeNode snapshotNode = (DefaultMutableTreeNode)dirNode.getChildAt(j);
				Snapshot snapshot = (Snapshot)snapshotNode.getUserObject();
				if(snapshot.getId().equals(id)){
					return snapshotNode;
				}
			}
		}
		throw new IllegalStateException("can't find node by id "+id);
	}



}
