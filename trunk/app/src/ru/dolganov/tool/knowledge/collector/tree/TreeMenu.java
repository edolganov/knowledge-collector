package ru.dolganov.tool.knowledge.collector.tree;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.Dir;
import model.knowledge.NodeMeta;
import model.knowledge.TextData;

import ru.chapaj.util.swing.IconHelper;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.dolganov.tool.knowledge.collector.actions.Actions;
import ru.dolganov.tool.knowledge.collector.tree.cell.HasCellConst;
import ru.dolganov.tool.knowledge.collector.tree.dialog.DialogOps;

public class TreeMenu extends JPopupMenu implements HasCellConst {
	
	ExtendTree tree;
	JMenuItem delete = new JMenuItem("delete",IconHelper.get("/images/kc/tree/menu/delete.png"));
	
	JMenuItem info = new JMenuItem();
	
	JMenu addMenu = new JMenu("add");
	JMenuItem dir = new JMenuItem("dir",IconHelper.get("/images/kc/tree/dir.png"));
	JMenuItem note = new JMenuItem("note",IconHelper.get("/images/kc/tree/note.png"));
	JMenuItem link = new JMenuItem("link",IconHelper.get("/images/kc/tree/netLink.png"));
	
	JMenu parent = new JMenu("parent ops");
	JMenuItem dirToParent = new JMenuItem("dir",IconHelper.get("/images/kc/tree/dir.png"));
	JMenuItem noteToParent = new JMenuItem("note",IconHelper.get("/images/kc/tree/note.png"));
	JMenuItem linkToParent = new JMenuItem("link",IconHelper.get("/images/kc/tree/netLink.png"));
	
	
	public TreeMenu(ExtendTree tree_) {
		super();
		tree = tree_;
		
		delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Actions.deleteCurrentTreeNode();
			}
			
		});
		
		dir.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Actions.addTreeNode(DialogOps.newDir());
			}
			
		});
		
		note.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Actions.addTreeNode(DialogOps.newText());
			}
			
		});
		
		link.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Actions.addTreeNode(DialogOps.newLink());
			}
			
		});
		
		addMenu.add(dir);
		addMenu.add(note);
		addMenu.add(link);
		
		dirToParent.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				NodeMeta parent = tree.getParentObject(tree.getCurrentNode(), NodeMeta.class);
				Actions.addTreeNode(parent,DialogOps.newDir());
			}
			
		});
		
		noteToParent.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				NodeMeta parent = tree.getParentObject(tree.getCurrentNode(), NodeMeta.class);
				Actions.addTreeNode(parent,DialogOps.newText());
			}
			
		});
		
		linkToParent.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				NodeMeta parent = tree.getParentObject(tree.getCurrentNode(), NodeMeta.class);
				Actions.addTreeNode(parent,DialogOps.newLink());
			}
			
		});
		
		parent.add(dirToParent);
		parent.add(noteToParent);
		parent.add(linkToParent);
		

		
	}


	@Override
	public void show(Component invoker, int x, int y) {
		DefaultMutableTreeNode currentNode = tree.getCurrentNode();
		if(currentNode == null) return;
		
		
		Object ob = tree.getCurrentObject();
		if(Cell.BUTTONS == ob) return;
		
		removeAll();
		//info.setText(ob.toString());
		//add(info);
		
		if(ob instanceof Dir || ob instanceof TextData)addMenu.setEnabled(true);
		else addMenu.setEnabled(false);
		add(addMenu);
		
		if(currentNode.isRoot() 
				|| ((DefaultMutableTreeNode)currentNode.getParent()).isRoot())
			parent.setEnabled(false);
		else parent.setEnabled(true);
		add(parent);
		addSeparator();
		add(delete);
		
		
		
		super.show(invoker, x, y);
	}
	
	

}
