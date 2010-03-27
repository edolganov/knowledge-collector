package ru.dolganov.tool.knowledge.collector.snapshot;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;

import ru.chapaj.util.swing.IconHelper;
import ru.chapaj.util.swing.tree.ExtendTree;

public class TreeMenu extends JPopupMenu {
	
	ExtendTree tree;
	JMenuItem delete = new JMenuItem("delete",IconHelper.get("/images/kc/tree/menu/delete.png"));
	JMenuItem resnap = new JMenuItem("resnaphot");
	JMenuItem rename = new JMenuItem("rename");
	
	boolean showInfo = true;
	SnapshotController owner;
	
	public TreeMenu(ExtendTree tree_, SnapshotController snapshotController) {
		super();
		tree = tree_;
		owner = snapshotController;
		
		delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				owner.deleteCurrentElement();
			}
			
		});
		
		resnap.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				owner.resnapCurrentSnapshot();
			}
			
		});

		rename.setEnabled(false);
	}


	@Override
	public void show(Component invoker, int x, int y) {
		DefaultMutableTreeNode currentNode = tree.getCurrentNode();
		if(currentNode == null) return;
		
		
		Object ob = tree.getCurrentObject();
		
		removeAll();
		if(ob instanceof TreeSnapshotDir){
			add(rename);
		}
		else if(ob instanceof TreeSnapshot){
			add(rename);
			add(resnap);
		}
		
		addSeparator();
		add(delete);
		
		
		
		super.show(invoker, x, y);
	}
	
	

}
