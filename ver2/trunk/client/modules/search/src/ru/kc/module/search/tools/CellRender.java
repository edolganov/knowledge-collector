package ru.kc.module.search.tools;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import ru.kc.common.node.NodeIcon;
import ru.kc.model.Node;


@SuppressWarnings("serial")
public class CellRender extends DefaultTreeCellRenderer {
	
	public CellRender() {
		super();
		putClientProperty("html.disable", Boolean.TRUE);

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
		else if(ob instanceof Group){
			Group group = (Group) ob;
			//Icon icon = NodeIcon.getIcon(group.type);
			String text = group.type.getSimpleName() + " ("+group.nodes.size()+")";
			setIcon(null);
			setText(text);
		}
		else if(ob instanceof Node){
			Node node = (Node) ob;
			Icon icon = NodeIcon.getIcon(node);
			String text = node.getName();
			setIcon(icon);
			setText(text);
		}
		return this;
		
	}

}
