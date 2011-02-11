package ru.kc.main.node;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.kc.main.common.Controller;
import ru.kc.main.node.ui.dialog.DirDialog;
import ru.kc.main.node.ui.dialog.NodePanel;
import ru.kc.model.Dir;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.Check;
import ru.kc.util.swing.keyboard.EnterKey;

@Mapping(DirDialog.class)
public class CreateDirController extends Controller<DirDialog>{

	NodePanel panel;
	Dir dir;
	
	@Override
	protected void init() {
		ui.setTitle("Create dir");
		panel = ui.panel;
		
		ui.okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createDirRequest();

			}
		});
		panel.name.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction() {
				createDirRequest();
			}
		});
	}

	protected void createDirRequest() {
		String name = panel.name.getText();
		if(!Check.isEmpty(name)){
			dir = persistFactory.createDir(name, null);
			ui.dispose();
		}
	}

	public Dir getDir() {
		return dir;
	}

}
