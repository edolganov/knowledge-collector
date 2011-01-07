/**
 * 
 */
package ru.chapaj.util.swing.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import ru.chapaj.util.swing.tree.DNDTree.DNDListener;

public interface TreeNodeListener extends DNDListener {
	
	void onDoubleClick(DefaultMutableTreeNode node);
	
	void onNodeMoveUpRequest();
	
	void onNodeMoveDownRequest();
	
	void onNodeSelect(DefaultMutableTreeNode node);
	
}