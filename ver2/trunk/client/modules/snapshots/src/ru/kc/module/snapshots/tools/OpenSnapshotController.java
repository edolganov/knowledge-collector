package ru.kc.module.snapshots.tools;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import ru.kc.common.controller.Controller;
import ru.kc.module.snapshots.command.OpenSnapshot;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.keyboard.EnterKey;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class OpenSnapshotController extends Controller<SnapshotsPanel> {

	private TreeFacade treeFacade;
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		
		ui.tree.addMouseListener(new MouseAdapter(){
			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Snapshot node = treeFacade.getCurrentObject(Snapshot.class);
				if(node == null) return; 
				
				if(e.getClickCount() == 2 && treeFacade.isOnSelectedElement(e.getX(), e.getY())){
					invokeSafe(new OpenSnapshot(node));
				}
			}
			
		});
		
		ui.tree.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				Snapshot node = treeFacade.getCurrentObject(Snapshot.class);
				if(node == null) return; 
				invokeSafe(new OpenSnapshot(node));
			}
		});
	}


}
