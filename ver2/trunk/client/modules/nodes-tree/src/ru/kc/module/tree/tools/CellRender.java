package ru.kc.module.tree.tools;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import ru.kc.main.model.NodeIcon;
import ru.kc.model.Node;
import ru.kc.util.swing.tree.TreeFacade;


public class CellRender extends DefaultTreeCellRenderer {
	
	TreeFacade treeFacade;
	
	public CellRender(JTree tree) {
		super();
		this.treeFacade = new TreeFacade(tree);
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		Object ob = ((DefaultMutableTreeNode)value).getUserObject();
		if(ob instanceof Node){
			Node node = (Node) ob;
			Icon icon = NodeIcon.getIcon(node);
			String text = node.getName();
			
			setIcon(icon);
			setText(text);
		}
		return this;
		
	}
	
	@Override
	public Icon getLeafIcon() {
		Node node = treeFacade.getCurrentObject(Node.class);
		if(node != null){
			return NodeIcon.getIcon(node);
		}
		return super.getLeafIcon();
	}
	
	@Override
	public Icon getOpenIcon() {
		Node node = treeFacade.getCurrentObject(Node.class);
		if(node != null){
			return NodeIcon.getIcon(node);
		}
		return super.getOpenIcon();
	}
	
	@Override
	public Icon getClosedIcon() {
		Node node = treeFacade.getCurrentObject(Node.class);
		if(node != null){
			return NodeIcon.getIcon(node);
		}
		return super.getClosedIcon();
	}

}
