package ru.kc.module.snapshots.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.kc.common.controller.Controller;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class UpDownController extends Controller<SnapshotsPanel>{

	private TreeFacade treeFacade;
	
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
	}
	
	private void moveUp() {
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
	
	private void moveDown() {
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
