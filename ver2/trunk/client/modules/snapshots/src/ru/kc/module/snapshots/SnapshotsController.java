package ru.kc.module.snapshots;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ru.kc.common.controller.Controller;
import ru.kc.components.dialog.OneTextFieldModule;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.util.Check;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class SnapshotsController extends Controller<SnapshotsPanel>{

	private static final String SNAPSHOTS_PROPERTY_KEY = "snapshots";
	
	private TreeFacade treeFacade;
	private Node owner;
	private List<SnapshotDir> snapshots = new ArrayList<SnapshotDir>();
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		ui.tree.setModel(TreeFacade.createModelByUserObject(""));
		ui.tree.setRootVisible(false);
		treeFacade.setSingleSelection();
		
		ui.addFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createDir();
			}
		});
		
		buildTree();
	}

	private void buildTree() {
		try {
			Node root = persistTree.getRoot();
			DefaultMutableTreeNode treeRoot = treeFacade.getRoot();
			loadSnapshots(root);
			for(SnapshotDir dir : snapshots){
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
			
		}catch (Exception e) {
			log.error("", e);
		}
	}
	
	private void loadSnapshots(Node node) {
		List<SnapshotDir> snapshots = getSnaphots(node);
		this.snapshots = snapshots;
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
	
	private void saveSnapshots() throws Exception {
		String data = new Gson().toJson(snapshots);
		updater.update(owner, new SetProperty(SNAPSHOTS_PROPERTY_KEY, data));
		
	}
	
	
	
	private void createDir() {
		try {
			OneTextFieldModule module = new OneTextFieldModule();
			module.createDialog(rootUI, true);
			module.setTitle("Create snapshot dir");
			module.setFieldName("name");
			module.show();
			String name = module.getText();
			if(Check.isEmpty(name))
				return;
			
			SnapshotDir dir = new SnapshotDir();
			dir.setName(name);
			
			DefaultMutableTreeNode beforeInsert = findBeforeInsertElement(dir);
			int insertIndex = 0;
			if(beforeInsert != null){
				insertIndex = findIndexInParent(beforeInsert) + 1;
			}
			insertDir(dir, insertIndex);
			addToTree(beforeInsert, dir);
		} catch (Exception e) {
			log.error("",e);
		}
	}

	private DefaultMutableTreeNode findBeforeInsertElement(SnapshotDir dir) {
		DefaultMutableTreeNode before = getLastRootElement();
		DefaultMutableTreeNode currentNode = treeFacade.getCurrentNode();
		if(currentNode != null){
			Object ob = currentNode.getUserObject();
			if(ob instanceof Snapshot){
				currentNode = (DefaultMutableTreeNode)currentNode.getParent();
			}
			before = currentNode;
		}
		return before;
	}


	private DefaultMutableTreeNode getLastRootElement() {
		DefaultMutableTreeNode root = treeFacade.getRoot();
		int childCount = root.getChildCount();
		if(childCount == 0)
			return null;
		else 
			return (DefaultMutableTreeNode) root.getChildAt(childCount-1);
	}
	
	
	private int findIndexInParent(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		for(int i = 0; i < parent.getChildCount(); i++){
			if(parent.getChildAt(i).equals(node)){
				return i;
			}
		}
		
		return -1;
	}
	
	private void insertDir(SnapshotDir dir, int insertIndex) throws Exception {
		snapshots.add(insertIndex, dir);
		saveSnapshots();
	}
	
	


	private void addToTree(DefaultMutableTreeNode beforeInsert, SnapshotDir dir) {
		int index = 0;
		if(beforeInsert != null){
			index = findIndexInParent(beforeInsert) + 1;
		}
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) beforeInsert.getParent();
		parent.insert(new DefaultMutableTreeNode(dir), index);
		
	}




}
