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

import ru.kc.common.controller.Controller;
import ru.kc.common.tree.TreeService;
import ru.kc.common.tree.event.GetTreeServiceRequest;
import ru.kc.model.Node;
import ru.kc.module.snapshots.event.OpenSnapshotRequest;
import ru.kc.module.snapshots.event.SaveSnapshotDirRequest;
import ru.kc.module.snapshots.event.SaveSnapshotRequest;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.tools.CellRender;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
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
	private Node owner;
	private List<SnapshotDir> snapshotDirs = new ArrayList<SnapshotDir>();
	
	@Override
	protected void init() {
		ui.remove.setToolTipText("Delete  (Delete)");
		
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
		
		
		
		TreeService service = invokeSafe(new GetTreeServiceRequest()).result;
		if(service != null){
			Node node = service.getRoot().getUserObject(Node.class);
			if(node != null){
				buildTree(node);
			}
		}

	}

	private void buildTree(Node node) {
		treeFacade.clear();
		DefaultMutableTreeNode treeRoot = treeFacade.getRoot();
		loadSnapshots(node);
		for(SnapshotDir dir : snapshotDirs){
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
	
	private void loadSnapshots(Node node) {
		List<SnapshotDir> snapshots = getSnaphots(node);
		this.snapshotDirs = snapshots;
		this.owner = node;
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
	public void onSaveRequest(SaveSnapshotDirRequest request) throws Exception {
		SnapshotDir dir = request.newDir;
		int insertIndex = request.index;
		snapshotDirs.add(insertIndex, dir);
		saveSnapshots();
	}
	
	@EventListener
	public void onSaveRequest(SaveSnapshotRequest request)throws Exception {
		int dirIndex = request.snapshotDirIndex;
		Snapshot newSnapshot = request.snapshot;
		
		SnapshotDir parentDir = snapshotDirs.get(dirIndex);
		if(parentDir != null){
			parentDir.add(newSnapshot);
			saveSnapshots();
		}
	}

	@Override
	protected void onNodeUpdated(Node old, Node updatedNode, Collection<UpdateRequest> updates) {
		if(old.equals(owner)){
			buildTree(updatedNode);
		}
	}


	protected void delete() {
		Object ob = treeFacade.getCurrentObject();
		if(ob == null)
			return;
		
		try {
			if(ob instanceof SnapshotDir){
				deleteDir();
			}
			else if(ob instanceof Snapshot){
				deleteSnapshot();
			}
		}catch (Exception e) {
			log.error("", e);
		}
	}
	
	private void deleteDir() throws Exception {
		DefaultMutableTreeNode node = treeFacade.getCurrentNode();
		SnapshotDir dir = (SnapshotDir) node.getUserObject();
		boolean confirm = dialogs.confirmByDialog(rootUI, "Confirm the operation","Delete "+dir.getName()+"?");
		if(confirm){
			snapshotDirs.remove(dir);
			saveSnapshots();
		}
	}


	private void deleteSnapshot() throws Exception {
		DefaultMutableTreeNode node = treeFacade.getCurrentNode();
		Snapshot snapshot = (Snapshot) node.getUserObject();
		boolean confirm = dialogs.confirmByDialog(rootUI, "Confirm the operation","Delete "+snapshot.getName()+"?");
		if(confirm){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
			SnapshotDir dir = (SnapshotDir)parent.getUserObject();
			dir.getSnapshots().remove(snapshot);
			saveSnapshots();
		}
	}

	protected void open(Snapshot node) {
		invokeSafe(new OpenSnapshotRequest(node));
	}


	private void saveSnapshots() throws Exception {
		String data = new Gson().toJson(snapshotDirs);
		updater.update(owner, new SetProperty(SNAPSHOTS_PROPERTY_KEY, data));
	}


}
