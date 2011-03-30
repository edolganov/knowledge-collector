package ru.kc.module.snapshots.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.components.dialog.OneTextFieldModule;
import ru.kc.module.snapshots.command.CreateSnapshotDir;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.Check;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class CreateDirController extends Controller<SnapshotsPanel> {

	private TreeFacade treeFacade;
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		
		ui.addFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createDir();
			}
		});
	}
	
	private void createDir() {
		String name = getNameForDir();
		if(Check.isEmpty(name)){
			return;
		}
		createDir(name);
	}

	private String getNameForDir() {
		OneTextFieldModule module = new OneTextFieldModule();
		module.createDialog(rootUI, true);
		module.setTitle("Create snapshot dir");
		module.setFieldName("name");
		module.show();
		String name = module.getText();
		return name;
	}
	
	
	private void createDir(String name) {
		
		DefaultMutableTreeNode beforeInsert = findBeforeInsertElement();
		int insertIndex = 0;
		if(beforeInsert != null){
			insertIndex = findIndexInParent(beforeInsert) + 1;
		}
		invokeSafe(new CreateSnapshotDir(name, insertIndex));
	}
	
	private DefaultMutableTreeNode findBeforeInsertElement() {
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
	
	
	private int findIndexInParent(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		for(int i = 0; i < parent.getChildCount(); i++){
			if(parent.getChildAt(i).equals(node)){
				return i;
			}
		}
		
		return -1;
	}

	private DefaultMutableTreeNode getLastRootElement() {
		DefaultMutableTreeNode root = treeFacade.getRoot();
		int childCount = root.getChildCount();
		if(childCount == 0)
			return null;
		else 
			return (DefaultMutableTreeNode) root.getChildAt(childCount-1);
	}

}
