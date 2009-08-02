package ru.chapaj.tool.link.collector.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ru.chapaj.tool.link.collector.App;
import ru.chapaj.tool.link.collector.AppUtil;
import ru.chapaj.tool.link.collector.model.Dir;
import ru.chapaj.tool.link.collector.model.Link;
import ru.chapaj.tool.link.collector.ui.AppFrame;
import ru.chapaj.util.Check;

public class NodeInfoController extends Controller<AppFrame> {
	
	private AppFrame ui;
	
	@Override
	public void init(final AppFrame ui) {
		this.ui = ui;
		hide();
		ActionListener updateAction = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = null,url = null,description = null;
				if(ui.name.isVisible()){
					name = ui.name.getText();
				}
				if(ui.url.isVisible()){
					url = ui.url.getText();
				}
				description = ui.description.getText();
				if(Check.isEmpty(description)) description = null;
				App.getDefault().getTreeController().updateCurNode(name,url,description);
				refreshDescriptionLink(description);
			}
			
		};
		
		ui.update.addActionListener(updateAction);
		ui.update2.addActionListener(updateAction);
		
		ui.linkDescription.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ui.metaTabs.setSelectedIndex(1);
			}
			
		});
	}
	
	public void hide(){
		ui.metaTabs.setSelectedIndex(0);
		// [17.05.2009] chapaj: на видимости этих полей завязана функция обновления полей
		ui.name.setVisible(false);
		ui.url.setVisible(false);
		ui.update.setVisible(false);
		ui.metaTabs.setEnabledAt(0, false);
		ui.metaTabs.setEnabledAt(1, false);
		ui.linkDescription.setVisible(false);
	}
	
	private void show(){
		ui.update.setVisible(true);
		ui.metaTabs.setEnabledAt(0, true);
		ui.metaTabs.setEnabledAt(1, true);
		ui.linkDescription.setVisible(true);
	}

	public void info(Object data) {
		hide();
		if (AppUtil.isDir(data)) {
			show();
			Dir dir = (Dir) data;
			if(dir.getMeta() != null){
				ui.name.setText(dir.getMeta().getName());
				ui.name.setVisible(true);
				ui.description.setText(dir.getMeta().getDescription());
				refreshDescriptionLink(dir.getMeta().getDescription());
			}
		}
		else if (AppUtil.isLink(data)) {
			show();
			Link link = (Link) data;
			ui.url.setText(link.getUrl());
			ui.name.setText(link.getName());
			ui.url.setVisible(true);
			ui.name.setVisible(true);
			ui.description.setText(link.getDescription());
			refreshDescriptionLink(link.getDescription());
		}
		
	}
	
	private void refreshDescriptionLink(String s){
		if(Check.isEmpty(s))
			ui.linkDescription.setInfo("empty");
		else ui.linkDescription.setInfo(null);
	}
	



}
