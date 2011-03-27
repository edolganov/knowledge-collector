package ru.kc.module.snapshots.tools;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;


@SuppressWarnings("serial")
public class CellRender extends DefaultTreeCellRenderer {
	
	public CellRender() {
		super();

	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		Object ob = ((DefaultMutableTreeNode)value).getUserObject();
		if(ob instanceof String){
			setIcon(null);
			setText(ob.toString());
		}
		else if(ob instanceof SnapshotDir){
			SnapshotDir dir = (SnapshotDir) ob;
			setText(dir.getName());
			if (expanded) {
	            setIcon(getOpenIcon());
	        } else {
	        	setIcon(getClosedIcon());
	        }
		}
		else if(ob instanceof Snapshot){
			Snapshot dir = (Snapshot) ob;
			setText(dir.getName());
		}
		return this;
		
	}

}
