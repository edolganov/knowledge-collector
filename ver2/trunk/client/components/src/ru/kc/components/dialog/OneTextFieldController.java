package ru.kc.components.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import ru.kc.common.controller.Controller;
import ru.kc.components.dialog.ui.OneTextFieldDialog;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.keyboard.EnterKey;

@Mapping(OneTextFieldDialog.class)
public class OneTextFieldController extends Controller<OneTextFieldDialog>{
	
	private String outText;

	@Override
	protected void init() {
		ui.okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				outText = ui.panel.field.getText();
				ui.dispose();

			}
		});
		ui.panel.field.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				outText = ui.panel.field.getText();
				ui.dispose();
			}
		});
	}
	
	public String getText(){
		return outText;
	}

}
