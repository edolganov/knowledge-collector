package ru.dolganov.tool.knowledge.collector.main;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import model.knowledge.Link;
import model.knowledge.NodeMeta;

import ru.chapaj.tool.link.collector.AppUtil;
import ru.chapaj.util.Check;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.model.CompareUtil;
import ru.dolganov.tool.knowledge.collector.model.DecoratorUtil;

@ControllerInfo(target=MainWindow.class)
public class SearchController extends Controller<MainWindow> {
	
	MainWindow ui;

	//ui model
	DefaultListModel listModel = new DefaultListModel();

	@Override
	public void init(final MainWindow ui_) {
		this.ui = ui_;
		
		ui.searchF.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				if(KeyEvent.VK_ENTER == e.getKeyCode()){
					doSearch(ui.searchF.getText());
				}
			}
		});
		
		ui.searchList.setModel(listModel);
		ui.searchList.addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseClicked(MouseEvent e) {
		         if (e.getClickCount() == 2) {
		             int i = ui.searchList.locationToIndex(e.getPoint());
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
	
	@SuppressWarnings("unchecked")
	protected void doSearch(String query) {
		if(Check.isEmpty(query)) return;
		
		DefaultTreeModel model = ((DefaultTreeModel)ui.tree.getModel());
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		HashMap<Class<?>, ArrayList<DefaultMutableTreeNode>> map = new HashMap<Class<?>, ArrayList<DefaultMutableTreeNode>>();
		LinkedList<Step> queque = new LinkedList<Step>();
		for(int j=0; j < root.getChildCount(); ++j){
			queque.addLast(new Step((DefaultMutableTreeNode)root.getChildAt(j)));
		}
		
		while(!queque.isEmpty()){
			Step s = queque.removeFirst();
			DefaultMutableTreeNode node = s.node;
			Object obj = node.getUserObject();
			if(obj instanceof NodeMeta){
				NodeMeta l = (NodeMeta) obj;

				if(isFounded(l, query)){
					Class<? extends NodeMeta> clazz = l.getClass();
					ArrayList<DefaultMutableTreeNode> list = map.get(clazz);
					if(list == null){
						list = new ArrayList<DefaultMutableTreeNode>();
						map.put(clazz, list);
					}
					list.add(node);
				}
			}
			
			for(int j=0; j < node.getChildCount(); ++j){
				queque.addLast(new Step((DefaultMutableTreeNode)node.getChildAt(j)));
			}
		}
		
		//sort
		Class<?>[] keys = map.keySet().toArray(new Class<?>[0]);
		Arrays.sort(keys, new Comparator<Class<?>>(){

			@SuppressWarnings("unchecked")
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				return CompareUtil.compare(CompareUtil.index((Class<? extends NodeMeta>)o1), CompareUtil.index((Class<? extends NodeMeta>)o2));
			}
			
		});
		
		listModel.clear();
		for(Class<?> key : keys){
			ArrayList<DefaultMutableTreeNode> list = map.get(key);
			listModel.addElement("--------   "+DecoratorUtil.name((Class<? extends NodeMeta>)key)+" ("+list.size()+")");
			for(DefaultMutableTreeNode n : list){
				listModel.addElement(n);
			}
			listModel.addElement("       ");
		}
		
		if(listModel.size() == 0){
			listModel.addElement("no result");
		}
	}
	
	boolean isFounded(NodeMeta meta, String query){
		if(meta == null) return false;
		if(contains(meta.getName(), query)) return true;
		if(contains(meta.getDescription(), query)) return true;
		if(meta instanceof Link){
			if(contains(((Link)meta).getUrl(), query)) return true;
		}
		return false;
	}
	
	boolean contains(String a, String b){
		if(a == null) return false;
		else return a.toLowerCase().trim().contains(b.toLowerCase().trim());
	}
	

}
