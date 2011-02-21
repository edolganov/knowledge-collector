package ru.kc.common.node.create;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.ui.LinkDialog;
import ru.kc.common.node.ui.LinkPanel;
import ru.kc.model.Link;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.Check;
import ru.kc.util.swing.keyboard.EnterKey;

@Mapping(LinkDialog.class)
public class CreateLinkController extends Controller<LinkDialog>{

	LinkPanel panel;
	Link node;
	
	@Override
	protected void init() {
		ui.setTitle("Create link");
		panel = ui.panel;
		initExchangeButton();
		
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
	}

	private void initExchangeButton() {
		panel.replace.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = panel.name.getText();
				String url = panel.url.getText();
				
				panel.name.setText(url);
				panel.url.setText(name);
			}
		});
	}

	protected void createNodeRequest() {
		String name = panel.name.getText();
		String url = panel.url.getText();
		
		if(Check.isEmpty(name) && !Check.isEmpty(url)){
			name = new String(url);
		} 
		else if( !Check.isEmpty(name) && Check.isEmpty(url)){
			url = new String(name);
		}
		
		if(!Check.isEmpty(name)){
			node = persistFactory.createLink(name, url, null);
			ui.dispose();
		}
	}

	public Link getNode() {
		return node;
	}

}
