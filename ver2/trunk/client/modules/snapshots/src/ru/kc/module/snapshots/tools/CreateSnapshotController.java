package ru.kc.module.snapshots.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.components.dialog.OneTextFieldModule;
import ru.kc.module.snapshots.event.CreateSnapshotDirRequest;
import ru.kc.module.snapshots.event.CreateTreeNodesRequest;
import ru.kc.module.snapshots.event.SaveSnapshotRequest;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.TreeNode;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.Check;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class CreateSnapshotController extends Controller<SnapshotsPanel> {

	private TreeFacade treeFacade;
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		
		ui.addSnapshot.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createSnapshot();
			}
		});
		
	}
	
	
	protected void createSnapshot() {
		TreeNode root = invokeSafe(new CreateTreeNodesRequest()).result;
		if(root == null){
			return;
		}
		
		String name = getName();
		if(Check.isEmpty(name)){
			return;
		}
		
		Snapshot snapshot = new Snapshot();
		snapshot.setName(name);
		snapshot.setRoot(root);
		
		Integer dirIndex = findDirToSnapshot();
		if(dirIndex == null){
			dirIndex = invokeSafe(new CreateSnapshotDirRequest("main")).result;
		}
		
		if(dirIndex == null){
			return;
		}
		
		invokeSafe(new SaveSnapshotRequest(snapshot, dirIndex));
			
	}


	private String getName() {
		OneTextFieldModule module = new OneTextFieldModule();
		module.createDialog(rootUI, true);
		module.setTitle("Create snapshot");
		module.setFieldName("name");
		module.show();
		String name = module.getText();
		return name;
	}
	
	private Integer findDirToSnapshot() {
		DefaultMutableTreeNode treeNode = treeFacade.getCurrentNode();
		if(treeNode == null){
			DefaultMutableTreeNode root = treeFacade.getRoot();
			if(root.getChildCount() == 0){
				return null;
			}
			treeNode = (DefaultMutableTreeNode) root.getChildAt(0);
		}
		
		Object ob = treeNode.getUserObject();
		if(ob instanceof Snapshot){
			treeNode = (DefaultMutableTreeNode)treeNode.getParent();
		}
		
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)treeNode.getParent();
		return parent.getIndex(treeNode);
	}
	

}
