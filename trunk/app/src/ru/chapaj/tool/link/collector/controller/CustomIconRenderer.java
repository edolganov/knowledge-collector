package ru.chapaj.tool.link.collector.controller;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import ru.chapaj.tool.link.collector.AppUtil;

class CustomIconRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;
	
	ImageIcon dirIcon;
    ImageIcon linkIcon;
    Icon otherIcon;

    public CustomIconRenderer() {
    	dirIcon = new ImageIcon(CustomIconRenderer.class.getResource("/images/dirIcon.png"));
        linkIcon = new ImageIcon(CustomIconRenderer.class.getResource("/images/linkIcon.png"));
        otherIcon = MetalIconFactory.getTreeLeafIcon();
    }
 
    public Component getTreeCellRendererComponent(JTree tree,
      Object value,boolean sel,boolean expanded,boolean leaf,
      int row,boolean hasFocus) {
 
        super.getTreeCellRendererComponent(tree, value, sel, 
          expanded, leaf, row, hasFocus);
 
        Object obj = ((DefaultMutableTreeNode)value).getUserObject();
        if(AppUtil.isDir(obj)){
        	setIcon(dirIcon);
        }
        else if (AppUtil.isLink(obj)){
        	setIcon(linkIcon);
        }
        else {
        	setIcon(otherIcon);
        }
        return this;
    }
}