package ru.dolganov.tool.knowledge.collector.info;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.Dir;
import model.knowledge.Link;
import model.knowledge.NodeMeta;
import model.knowledge.TextData;

import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.actions.Actions;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;
import ru.dolganov.tool.knowledge.collector.model.HasNodeMetaParams;
import ru.chapaj.tool.link.collector.ui.component.PropertyTextAreaPanel;
import ru.chapaj.util.Check;
import ru.chapaj.util.swing.tree.ExtendTree.TreeNodeAdapter;

public class InfoController extends Controller<MainWindow> implements HasNodeMetaParams{
	static enum Mode {
		link,dir,text,none
	}
	
	MainWindow ui;
	
	BasicInfo basicInfo = new BasicInfo();
	LinkInfo linkInfo = new LinkInfo();
	NoteInfo noteInfo = new NoteInfo();
	Mode curMode = Mode.none;
	
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
		ActionListener actionListener = new  ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
			
		};
		basicInfo.jButton.addActionListener(actionListener);
		
		linkInfo.name.label.setText("name");
		linkInfo.description.label.setText("description");
		linkInfo.url.label.setText("url");
		initArea(linkInfo.description);
		linkInfo.jButton.addActionListener(actionListener);
		
		noteInfo.name.label.setText("name");
		noteInfo.description.label.setText("text");
		noteInfo.jButton.addActionListener(actionListener);
		initArea(noteInfo.description);
		
	}
	
	protected void update() {
		if(Mode.none == curMode) return;
		
		HashMap<String, String> params = new HashMap<String, String>(2);
		if(Mode.dir == curMode){
			params.put(Params.name.toString(), basicInfo.name.getText());
			params.put(Params.description.toString(), basicInfo.description.getText());
		}
		else if(Mode.link == curMode){
			params.put(Params.name.toString(), linkInfo.name.getText());
			params.put(Params.description.toString(), linkInfo.description.getText());
			params.put(Params.url.toString(), linkInfo.url.getText());
		}
		else if(Mode.text == curMode){
			params.put(Params.name.toString(), noteInfo.name.getText());
			params.put(Params.text.toString(), noteInfo.description.getText());
		}
		else return;
		
		Actions.updateCurrentTreeNode(params);
		
		
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
	

	

	protected void show(DefaultMutableTreeNode node) {
		hide();
		Object uo = node.getUserObject();
		if(! (uo instanceof NodeMeta)){
			hide();
			return;
		}
		
		NodeMeta ob = (NodeMeta)uo;
		if(ob instanceof Dir){
			curMode = Mode.dir;
			ui.infoPanel.add(basicInfo);
			basicInfo.name.setText(ob.getName());
			basicInfo.description.setText(ob.getDescription());
			checkEmptyArea(basicInfo.description);
		}
		else if(ob instanceof Link){
			curMode = Mode.link;
			Link l = (Link) ob;
			ui.infoPanel.add(linkInfo);
			linkInfo.name.setText(ob.getName());
			linkInfo.description.setText(ob.getDescription());
			linkInfo.url.setText(l.getUrl());
			checkEmptyArea(linkInfo.description);
		}
		else if(ob instanceof TextData){
			curMode = Mode.text;
			ui.infoPanel.add(noteInfo);
			noteInfo.name.setText(ob.getName());
			noteInfo.description.setText((String)dao.getExternalData(ob).get(Params.text.toString()));
			checkEmptyArea(noteInfo.description);
		}

		
	}

	protected void hide() {
		ui.infoPanel.removeAll();
		curMode = Mode.none;
	}

}
