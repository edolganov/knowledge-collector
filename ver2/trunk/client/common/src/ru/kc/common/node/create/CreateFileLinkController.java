package ru.kc.common.node.create;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.ui.FileLinkDialog;
import ru.kc.common.node.ui.FilePanel;
import ru.kc.model.FileLink;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.Check;
import ru.kc.util.swing.keyboard.EnterKey;

@Mapping(FileLinkDialog.class)
public class CreateFileLinkController extends Controller<FileLinkDialog>{

	FilePanel panel;
	FileLink node;
	
	@Override
	protected void init() {
		ui.setTitle("Create file link");
		panel = ui.panel;
		
		initFileChooserButton();
		
		ui.okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createNodeRequest();

			}
		});
		panel.name.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				createNodeRequest();
			}
		});
		panel.path.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				createNodeRequest();
			}
		});
	}

	private void initFileChooserButton() {
		ui.panel.fileChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(rootUI);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					panel.path.setText(file.getAbsolutePath());
				}
			}
		});
	}

	protected void createNodeRequest() {
		String name = panel.name.getText();
		String path = panel.path.getText();
		
		if(Check.isEmpty(name) && !Check.isEmpty(path)){
			name = new String(path);
		} 
		else if( !Check.isEmpty(name) && Check.isEmpty(path)){
			path = new String(name);
		}
		
		if(!Check.isEmpty(name)){
			node = persistFactory.createFileLink(name, path, null);
			ui.dispose();
		}
	}

	public FileLink getNode() {
		return node;
	}

}
