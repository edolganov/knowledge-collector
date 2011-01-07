package ru.chapaj.util.swing.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class ExtendDefaultTreeModel extends DefaultTreeModel {

	public ExtendDefaultTreeModel(DefaultMutableTreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
	}

	public ExtendDefaultTreeModel(TreeNode root) {
		super(root);
	}
	
	@Override
	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode)super.getRoot();
	}

}
