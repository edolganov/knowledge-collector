package ru.kc.module.snapshots.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.module.snapshots.command.DeleteSnapshot;
import ru.kc.module.snapshots.command.DeleteSnapshotDir;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.keyboard.DeleteKey;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class DeleteController extends Controller<SnapshotsPanel> {

	private TreeFacade treeFacade;
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		
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
	}
	
	private void delete() {
		Object ob = treeFacade.getCurrentObject();
		if(ob == null)
			return;
		
		if(ob instanceof SnapshotDir){
			SnapshotDir dir = (SnapshotDir)ob;
			invokeSafe(new DeleteSnapshotDir(dir));
		}
		else if(ob instanceof Snapshot){
			deleteSnapshot();
		}
	}


	private void deleteSnapshot() {
		DefaultMutableTreeNode node = treeFacade.getCurrentNode();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		Snapshot snapshot = (Snapshot) node.getUserObject();
		SnapshotDir dir = (SnapshotDir)parent.getUserObject();
		invokeSafe(new DeleteSnapshot(dir, snapshot));
	}

}
