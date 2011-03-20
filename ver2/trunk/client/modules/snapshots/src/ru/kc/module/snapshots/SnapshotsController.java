package ru.kc.module.snapshots;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ru.kc.common.controller.Controller;
import ru.kc.components.dialog.OneTextFieldModule;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.Check;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class SnapshotsController extends Controller<SnapshotsPanel>{

	private static final String SNAPSHOTS_PROPERTY_KEY = "snapshots";
	
	private TreeFacade treeFacade;
	private List<SnapshotDir> snapshots;
	
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

	private void createDir() {
		OneTextFieldModule module = new OneTextFieldModule();
		module.createDialog(rootUI, true);
		module.setTitle("Create snapshot dir");
		module.setFieldName("name");
		module.show();
		String name = module.getText();
		if(Check.isEmpty(name))
			return;
		System.out.println(name);
		
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
		try {
			List<SnapshotDir> snapshots = getSnaphots(node);
			this.snapshots = snapshots;
		}catch (Exception e) {
			this.snapshots = new ArrayList<SnapshotDir>(0);
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

}
