package ru.chapaj.tool.link.collector.controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import ru.chapaj.tool.link.collector.AppUtil;
import ru.chapaj.tool.link.collector.model.Dir;
import ru.chapaj.tool.link.collector.model.Link;
import ru.chapaj.tool.link.collector.model.NodeMeta;
import ru.chapaj.tool.link.collector.ui.AppFrame;
import ru.chapaj.util.Check;

public class SearchController extends Controller<AppFrame> {

	AppFrame ui;
	
	//ui model
	DefaultListModel listModel = new DefaultListModel();
	
	@Override
	public void init(final AppFrame ui) {
		this.ui = ui;
		ui.jButtonSearch.addActionListener(new java.awt.event.ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				doSearch(ui.jTextField.getText());
			}
		});
		
		ui.jTextField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				if(KeyEvent.VK_ENTER == e.getKeyCode()){
					doSearch(ui.jTextField.getText());
				}
			}
		});
		
		ui.jList.setModel(listModel);
		ui.jList.addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseClicked(MouseEvent e) {
		         if (e.getClickCount() == 2) {
		             int i = ui.jList.locationToIndex(e.getPoint());
		             Object ob = listModel.get(i);
		             if(ob instanceof String) return;
		             DefaultMutableTreeNode node = (DefaultMutableTreeNode) ob;
		             TreePath path = new TreePath(node.getPath());
		             ui.tree.scrollPathToVisible(path);
		             ui.tree.setSelectionPath(path);
		             
		          }
			}
		});
		
	}
	
	static class Step {
		DefaultMutableTreeNode node;

		public Step(DefaultMutableTreeNode dir) {
			super();
			this.node = dir;
		}

	}

	protected void doSearch(String query) {
		if(Check.isEmpty(query)) return;
		
		DefaultTreeModel model = ((DefaultTreeModel)ui.tree.getModel());
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		ArrayList<DefaultMutableTreeNode> resultDir = new ArrayList<DefaultMutableTreeNode>();
		ArrayList<DefaultMutableTreeNode> resultLink = new ArrayList<DefaultMutableTreeNode>();
		LinkedList<Step> queque = new LinkedList<Step>();
		for(int j=0; j < root.getChildCount(); ++j){
			queque.addLast(new Step((DefaultMutableTreeNode)root.getChildAt(j)));
		}
		
		while(!queque.isEmpty()){
			Step s = queque.removeFirst();
			DefaultMutableTreeNode node = s.node;
			Object obj = node.getUserObject();
			if(AppUtil.isDir(obj)){
				Dir d = (Dir) obj;
				if(isFounded(d.getMeta(), query)){
					resultDir.add(node);
				}
			}
			else if(AppUtil.isLink(obj)){
				Link l = (Link) obj;

				if(isFounded(l, query)){
					resultLink.add(node);
				}
			}
			
			for(int j=0; j < node.getChildCount(); ++j){
				queque.addLast(new Step((DefaultMutableTreeNode)node.getChildAt(j)));
			}
		}
		
		listModel.clear();
		listModel.addElement("--------   dirs ("+resultDir.size()+")");
		for(DefaultMutableTreeNode n : resultDir){
			listModel.addElement(n);
		}
		
		listModel.addElement("       ");
		listModel.addElement("--------   links ("+resultLink.size()+")");
		for(DefaultMutableTreeNode n : resultLink){
			listModel.addElement(n);
		}
		
		
	}
	
	boolean isFounded(NodeMeta meta, String query){
		if(meta == null) return false;
		if(contains(meta.getName(), query)) return true;
		//if(contains(meta.getDescription(), query)) return true;
		return false;
	}
	
	boolean isFounded(Link meta, String query){
		if(meta == null) return false;
		if(isFounded((NodeMeta)meta, query)) return true;
		if(contains(meta.getUrl(), query)) return true;
		return false;
	}
	
	boolean contains(String a, String b){
		if(a == null) return false;
		else return a.toLowerCase().trim().contains(b.toLowerCase().trim());
	}


}
