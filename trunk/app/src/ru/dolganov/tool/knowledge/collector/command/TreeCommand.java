package ru.dolganov.tool.knowledge.collector.command;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.Node;
import model.knowledge.RootElement;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.dolganov.tool.knowledge.collector.App;

public abstract class TreeCommand extends Command {
	
	protected ExtendTree tree = App.getDefault().getUI().tree;

	protected RootElement getCurNode(){
		Object ob = tree.getCurrentObject();
		if(ob == null) return null;
		
		if (ob instanceof RootElement) {
			return (RootElement) ob;
		}
		
		return null;
	}
	
	protected DefaultMutableTreeNode getTreeNode(){
		return tree.getCurrentNode();
	}
}
