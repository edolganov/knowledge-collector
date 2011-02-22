package ru.kc.common.node.create;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.ui.NodePanel;
import ru.kc.common.node.ui.TextDialog;
import ru.kc.model.Text;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.Check;
import ru.kc.util.swing.keyboard.EnterKey;

@Mapping(TextDialog.class)
public class CreateTextController extends Controller<TextDialog>{

	NodePanel panel;
	Text value;
	
	@Override
	protected void init() {
		ui.setTitle("Create text");
		panel = ui.panel;
		
		ui.okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createDirRequest();

			}
		});
		panel.name.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				createDirRequest();
			}
		});
	}

	protected void createDirRequest() {
		String name = panel.name.getText();
		if(!Check.isEmpty(name)){
			value = persistFactory.createText(name, null);
			ui.dispose();
		}
	}

	public Text getValue() {
		return value;
	}

}
