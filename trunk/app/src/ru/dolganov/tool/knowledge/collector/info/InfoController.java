package ru.dolganov.tool.knowledge.collector.info;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.Dir;
import model.knowledge.Link;
import model.knowledge.NodeMeta;
import model.knowledge.TextData;

import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;
import ru.chapaj.tool.link.collector.ui.component.PropertyTextAreaPanel;
import ru.chapaj.util.Check;
import ru.chapaj.util.swing.tree.ExtendTree.TreeNodeAdapter;

public class InfoController extends Controller<MainWindow>{

	MainWindow ui;
	
	BasicInfo basicInfo = new BasicInfo();
	LinkInfo linkInfo = new LinkInfo();
	NoteInfo noteInfo = new NoteInfo();
	
	@Override
	public void init(MainWindow ui_) {
		this.ui = ui_;
		ui.tree.addTreeNodeListener(new TreeNodeAdapter(){
			@Override
			public void onNodeSelect(DefaultMutableTreeNode node) {
				if(node == null) hide();
				else if(node.isRoot()) hide();
				else show(node);
				ui.infoPanel.validate();
				ui.infoPanel.repaint();
			}
		});
		
		basicInfo.name.label.setText("name");
		basicInfo.description.label.setText("description");
		initArea(basicInfo.description);
		
		linkInfo.name.label.setText("name");
		linkInfo.description.label.setText("description");
		linkInfo.url.label.setText("url");
		initArea(linkInfo.description);
		
		noteInfo.name.label.setText("name");
		noteInfo.description.label.setText("text");
		
	}
	
	Color empty = new Color(212,208,200);
	Color active = Color.WHITE;
	
	void initArea(final PropertyTextAreaPanel area){
		area.textArea.setBackground(empty);
		area.textArea.addFocusListener(new FocusAdapter(){
			
			@Override
			public void focusGained(FocusEvent e) {
				area.textArea.setBackground(active);
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				checkEmptyArea(area);
			}
		});
	}
	
	void checkEmptyArea(final PropertyTextAreaPanel area){
		if(!Check.isEmpty(area.getText())){
			area.textArea.setBackground(active);
		}
		else {
			area.textArea.setBackground(empty);
		}
	}
	
	DefaultMutableTreeNode curNode;
	

	protected void show(DefaultMutableTreeNode node) {
		hide();
		curNode = node;
		Object uo = node.getUserObject();
		if(! (uo instanceof NodeMeta)){
			hide();
			return;
		}
		
		NodeMeta ob = (NodeMeta)uo;
		if(ob instanceof Dir){
			ui.infoPanel.add(basicInfo);
			basicInfo.name.setText(ob.getName());
			basicInfo.description.setText(ob.getDescription());
			checkEmptyArea(basicInfo.description);
		}
		else if(ob instanceof Link){
			Link l = (Link) ob;
			ui.infoPanel.add(linkInfo);
			linkInfo.name.setText(ob.getName());
			linkInfo.description.setText(ob.getDescription());
			linkInfo.url.setText(l.getUrl());
			checkEmptyArea(linkInfo.description);
		}
		else if(ob instanceof TextData){
			ui.infoPanel.add(noteInfo);
			noteInfo.name.setText(ob.getName());
			noteInfo.description.setText(ob.getDescription());
		}

		
	}

	protected void hide() {
		ui.infoPanel.removeAll();
	}

}
