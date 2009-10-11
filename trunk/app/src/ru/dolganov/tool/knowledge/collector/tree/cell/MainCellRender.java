package ru.dolganov.tool.knowledge.collector.tree.cell;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import model.knowledge.Dir;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.Note;
import ru.chapaj.util.swing.IconHelper;

public class MainCellRender implements TreeCellRenderer, HasCellConst {

	NodeCellRender nodeCellRender = new NodeCellRender();
	NodeButtons nodeButtons = new NodeButtons();
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Object obj = ((DefaultMutableTreeNode)value).getUserObject();
		if(Cell.BUTTONS == obj){
			return nodeButtons.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
		else return nodeCellRender.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
	}
	
}

class NodeButtons implements TreeCellRenderer {

	NodeButtonsPanelExtend renderer;
	DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
	Color backgroundSelectionColor;
	Color backgroundNonSelectionColor;

	public NodeButtons() {
		renderer = new NodeButtonsPanelExtend();
		backgroundSelectionColor = defaultRenderer
				.getBackgroundSelectionColor();
		backgroundNonSelectionColor = defaultRenderer
				.getBackgroundNonSelectionColor();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		return renderer;
	}

}

class NodeCellRender extends DefaultTreeCellRenderer {
	
	ImageIcon dir = IconHelper.get("/images/kc/tree/dir.png");
    ImageIcon netLink = IconHelper.get("/images/kc/tree/netLink.png");
    ImageIcon localLink = IconHelper.get("/images/kc/tree/localLink.png");
    ImageIcon note = IconHelper.get("/images/kc/tree/note.png");
    //Icon otherIcon = MetalIconFactory.getTreeLeafIcon();
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		Object obj = ((DefaultMutableTreeNode)value).getUserObject();
		if(obj instanceof Dir){
			setIcon(dir);
		}
		else if(obj instanceof Note){
			setIcon(note);
		}
		else if(obj instanceof NetworkLink){
			setIcon(netLink);
		}
		else if(obj instanceof LocalLink){
			setIcon(localLink);
		}
		else {
			setIcon(getLeafIcon());
		}
		return this;
		
	}
}