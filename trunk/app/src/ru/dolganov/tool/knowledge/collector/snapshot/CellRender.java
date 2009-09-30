package ru.dolganov.tool.knowledge.collector.snapshot;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;

public class CellRender extends DefaultTreeCellRenderer{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		Object obj = ((DefaultMutableTreeNode)value).getUserObject();
		if(obj instanceof TreeSnapshot){
			setIcon(getLeafIcon());
		}
		else if(obj instanceof TreeSnapshotDir){
			setIcon(getClosedIcon());
		}
		else {
			setIcon(getLeafIcon());
		}
		return this;
		
	}

}
