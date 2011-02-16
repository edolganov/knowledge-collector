package ru.kc.module.tree.tools;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import ru.kc.common.node.NodeIcon;
import ru.kc.common.node.edit.NodeEditionsAggregator;
import ru.kc.model.Node;
import ru.kc.util.swing.tree.TreeFacade;


@SuppressWarnings("serial")
public class CellRender extends DefaultTreeCellRenderer {
	
	TreeFacade treeFacade;
	NodeEditionsAggregator nodeEditionsAggregator;
	Font standartFont;
	Font boldFont;
	
	public CellRender(JTree tree, NodeEditionsAggregator nodeEditionsAggregator) {
		super();
		this.treeFacade = new TreeFacade(tree);
		this.nodeEditionsAggregator = nodeEditionsAggregator;
		putClientProperty("html.disable", Boolean.TRUE);
		standartFont = new JLabel().getFont();
		boldFont = new Font(standartFont.getName(), Font.BOLD, standartFont.getSize());

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
			Icon icon = NodeIcon.getIcon(node);;
			String text = node.getName();
			if(nodeEditionsAggregator.hasEditions(node)){
				text = "* "+text;
				setFont(boldFont);
			} else {
				setFont(standartFont);
			}

			
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
