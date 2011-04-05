package ru.kc.module.snapshots.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import ru.kc.common.controller.Controller;
import ru.kc.module.snapshots.command.DeleteTreeObject;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.keyboard.DeleteKey;

@Mapping(SnapshotsPanel.class)
public class DeleteController extends Controller<SnapshotsPanel> {
	
	@Override
	protected void init() {
		
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
		invokeSafe(new DeleteTreeObject(ui.tree));
	}

}
