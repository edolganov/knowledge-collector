package ru.kc.module.snapshots;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import ru.kc.common.controller.Controller;
import ru.kc.model.Node;
import ru.kc.module.snapshots.command.GetOwner;
import ru.kc.module.snapshots.event.OpenSnapshotRequest;
import ru.kc.module.snapshots.event.SaveSnapshotDirRequest;
import ru.kc.module.snapshots.event.SaveSnapshotRequest;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.SnapshotCreated;
import ru.kc.module.snapshots.model.update.SnapshotDeleted;
import ru.kc.module.snapshots.model.update.SnapshotDirCreated;
import ru.kc.module.snapshots.model.update.SnapshotDirDeleted;
import ru.kc.module.snapshots.model.update.SnapshotsUpdate;
import ru.kc.module.snapshots.tools.CellRender;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.common.event.AppClosing;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.util.swing.keyboard.DeleteKey;
import ru.kc.util.swing.keyboard.EnterKey;
import ru.kc.util.swing.tree.TreeFacade;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Mapping(SnapshotsPanel.class)
public class SnapshotsController extends Controller<SnapshotsPanel>{

	private static final String SNAPSHOTS_PROPERTY_KEY = "snapshots";
	
	private TreeFacade treeFacade;
	
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
		
		ui.remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
		ui.tree.addKeyListener(new DeleteKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				delete();
			}
			
		});
		ui.tree.addMouseListener(new MouseAdapter(){
			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Snapshot node = treeFacade.getCurrentObject(Snapshot.class);
				if(node == null) return; 
				
				if(e.getClickCount() == 2 && treeFacade.isOnSelectedElement(e.getX(), e.getY())){
					open(node);
				}
			}
			
		});
		
		ui.tree.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				Snapshot node = treeFacade.getCurrentObject(Snapshot.class);
				if(node == null) return; 
				open(node);
			}
		});
		
		ui.up.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				moveUp();
			}
		});
		
		ui.down.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown();
			}
		});

		buildTree();
	}


	private void buildTree() {
		treeFacade.clear();
		
		Node owner = invokeSafe(new GetOwner(this)).result;
		if(owner == null) return;
		
		List<SnapshotDir> snaphots = getSnaphots(owner);
		DefaultMutableTreeNode treeRoot = treeFacade.getRoot();
		for(SnapshotDir dir : snaphots){
			DefaultMutableTreeNode dirNode = treeFacade.addChild(treeRoot, dir);
			for(Snapshot snapshot : dir.getSnapshots()){
				treeFacade.addChild(dirNode, snapshot);
			}
		}
		
		for(int i = 0; i < treeRoot.getChildCount(); ++i){
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)treeRoot.getChildAt(i);
			SnapshotDir dir = (SnapshotDir)child.getUserObject();
			if(dir.isOpen()){
				treeFacade.expand(child);
			}
		}
	}
	

	private List<SnapshotDir> getSnaphots(Node node) {
		String data = node.getProperty(SNAPSHOTS_PROPERTY_KEY);
		if(data == null)
			return new ArrayList<SnapshotDir>(0);
		
		Type listType = new TypeToken<List<SnapshotDir>>(){}.getType();
		List<SnapshotDir> list = new Gson().fromJson(data, listType);
		return list;
	}
	
	
	@EventListener
	public void onClosing(AppClosing event){
		saveSnapshots(null);
	}
	
	
	@EventListener
	public void onSaveRequest(SaveSnapshotDirRequest request) {
		SnapshotDir dir = request.newDir;
		int insertIndex = request.index;
		List<SnapshotDir> snapshotDirs = getDirs();
		snapshotDirs.add(insertIndex, dir);
		saveSnapshots(new SnapshotDirCreated(dir, insertIndex));
	}
	
	@EventListener
	public void onSaveRequest(SaveSnapshotRequest request) {
		int dirIndex = request.snapshotDirIndex;
		Snapshot newSnapshot = request.snapshot;
		
		List<SnapshotDir> snapshotDirs = getDirs();
		SnapshotDir parentDir = snapshotDirs.get(dirIndex);
		if(parentDir != null){
			parentDir.add(newSnapshot);
			saveSnapshots(new SnapshotCreated(parentDir, newSnapshot));
		}
	}

	

	protected void delete() {
		Object ob = treeFacade.getCurrentObject();
		if(ob == null)
			return;
		
		if(ob instanceof SnapshotDir){
			deleteDir();
		}
		else if(ob instanceof Snapshot){
			deleteSnapshot();
		}
	}
	
	private void deleteDir() {
		DefaultMutableTreeNode node = treeFacade.getCurrentNode();
		SnapshotDir dir = (SnapshotDir) node.getUserObject();
		boolean confirm = dialogs.confirmByDialog(rootUI, "Confirm the operation","Delete "+dir.getName()+"?");
		if(confirm){
			List<SnapshotDir> snapshotDirs = copyModel();
			snapshotDirs.remove(dir);
			saveSnapshots(snapshotDirs, new SnapshotDirDeleted(dir));
		}
	}


	private void deleteSnapshot() {
		DefaultMutableTreeNode node = treeFacade.getCurrentNode();
		Snapshot snapshot = (Snapshot) node.getUserObject();
		boolean confirm = dialogs.confirmByDialog(rootUI, "Confirm the operation","Delete "+snapshot.getName()+"?");
		if(confirm){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
			SnapshotDir dir = (SnapshotDir)parent.getUserObject();
			dir.getSnapshots().remove(snapshot);
			saveSnapshots(new SnapshotDeleted(snapshot));
		}
	}

	protected void open(Snapshot node) {
		invokeSafe(new OpenSnapshotRequest(node));
	}


	private void saveSnapshots(List<SnapshotDir> snapshotDirs, SnapshotsUpdate additionInfo) {
		Node owner = invokeSafe(new GetOwner(this)).result;
		if(owner == null) return;
		String data = new Gson().toJson(snapshotDirs);
		try {
			updater.update(owner, new SetProperty(SNAPSHOTS_PROPERTY_KEY, data, additionInfo));
		} catch (Exception e) {
			log.error("save error", e);
			revert();
		}
	}
	

	private List<SnapshotDir> copyModel() {
		ArrayList<SnapshotDir> out = new ArrayList<SnapshotDir>();
		DefaultMutableTreeNode root = treeFacade.getRoot();
		for(DefaultMutableTreeNode treeNode : treeFacade.getChildren(root)){
			out.add((SnapshotDir)treeNode.getUserObject());
		}
		synchronizedOpenDirs(out);
		return out;
	}

	private void synchronizedOpenDirs(List<SnapshotDir> snapshotDirs) {
		for(SnapshotDir dir : snapshotDirs){
			DefaultMutableTreeNode node = findNode(dir.getId());
			if(node != null){
				if(treeFacade.isExpanded(node)){
					dir.setOpen(true);
				} else {
					dir.setOpen(false);
				}
			} else {
				dir.setOpen(false);
			}
		}
		
	}
	
	private void revert() {
		buildTree();
	}

	@Override
	protected void onNodeUpdated(Node old, Node updatedNode, Collection<UpdateRequest> nodeUpdates) {
		Node owner = invokeSafe(new GetOwner(this)).result;
		if(owner == null) return;
		
		if(old.equals(owner)){
			
			List<SnapshotsUpdate> updates = findUpdates(nodeUpdates);
			if(updates.size() > 0){
				synchronizedTree(updates);
			}

		}
	}

	private List<SnapshotsUpdate> findUpdates(Collection<UpdateRequest> nodeUpdates) {
		ArrayList<SnapshotsUpdate> out = new ArrayList<SnapshotsUpdate>();
		for(UpdateRequest nodeUpdate : nodeUpdates){
			if(nodeUpdate instanceof SetProperty){
				Object additionInfo = ((SetProperty) nodeUpdate).additionInfo;
				if(additionInfo instanceof SnapshotsUpdate){
					out.add((SnapshotsUpdate)additionInfo);
				}
			}
		}
		return out;
	}

	private void synchronizedTree(List<SnapshotsUpdate> updates) {
		for(SnapshotsUpdate update : updates){
			synchronizedTree(update);
		}
	}

	private void synchronizedTree(SnapshotsUpdate update) {
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
		return null;
	}
	
	
	protected void moveUp() {
		Object ob = treeFacade.getCurrentObject();
		if(ob == null){
			return;
		}
		
		if(ob instanceof SnapshotDir){
			moveSnapshotDirUp();
		}
		else if(ob instanceof Snapshot){
			moveSnapshotUp();
		}
	}
	
	private void moveSnapshotDirUp() {
		SnapshotDir dir = treeFacade.getCurrentObject(SnapshotDir.class);
//		snapshotDirs.re
		
	}


	private void moveSnapshotUp() {
		// TODO Auto-generated method stub
		
	}
	
	protected void moveDown() {
		Object ob = treeFacade.getCurrentObject();
		if(ob == null){
			return;
		}
		
		if(ob instanceof SnapshotDir){
			moveSnapshotDirDown();
		}
		else if(ob instanceof Snapshot){
			moveSnapshotDown();
		}
	}
	
	private void moveSnapshotDirDown() {
		SnapshotDir dir = treeFacade.getCurrentObject(SnapshotDir.class);
//		snapshotDirs.re
		
	}


	private void moveSnapshotDown() {
		// TODO Auto-generated method stub
		
	}





}
