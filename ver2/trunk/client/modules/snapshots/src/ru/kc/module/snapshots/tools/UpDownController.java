package ru.kc.module.snapshots.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import ru.kc.common.controller.Controller;
import ru.kc.module.snapshots.command.MoveSnapshotDirDown;
import ru.kc.module.snapshots.command.MoveSnapshotDirUp;
import ru.kc.module.snapshots.command.MoveSnapshotDown;
import ru.kc.module.snapshots.command.MoveSnapshotUp;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.component.ComponentUtil;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class UpDownController extends Controller<SnapshotsPanel>{

	private TreeFacade treeFacade;
	
	@SuppressWarnings("serial")
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		
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
		
		ComponentUtil.addAction(ui.tree, "control UP", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				moveUp();
			}
		});
		
		ComponentUtil.addAction(ui.tree, "control DOWN", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown();
			}
		});
	}
	
	private void moveUp() {
		Object ob = treeFacade.getCurrentObject();
		if(ob == null){
			return;
		}
		
		if(ob instanceof SnapshotDir){
			SnapshotDir dir = (SnapshotDir)ob;
			invokeSafe(new MoveSnapshotDirUp(dir));
		}
		else if(ob instanceof Snapshot){
			Snapshot snap = (Snapshot)ob;
			SnapshotDir dir = (SnapshotDir)treeFacade.getParentOfCurrentNode().getUserObject();
			invokeSafe(new MoveSnapshotUp(dir, snap));
		}
	}
	
	private void moveDown() {
		Object ob = treeFacade.getCurrentObject();
		if(ob == null){
			return;
		}
		
		if(ob instanceof SnapshotDir){
			SnapshotDir dir = (SnapshotDir)ob;
			invokeSafe(new MoveSnapshotDirDown(dir));
		}
		else if(ob instanceof Snapshot){
			Snapshot snap = (Snapshot)ob;
			SnapshotDir dir = (SnapshotDir)treeFacade.getParentOfCurrentNode().getUserObject();
			invokeSafe(new MoveSnapshotDown(dir, snap));
		}
	}
	
}
