package ru.kc.util.swing.tree;

import javax.swing.*;
import javax.swing.tree.*;

import java.util.ArrayList;
import java.util.Collections;

public class TreeFacade {

	public final JTree tree;

    public TreeFacade(JTree tree) {
        this.tree = tree;
    }

    public JTree tree() {
        return tree;
    }

    public boolean isOnSelectedElement(int x, int y) {
        return TreeUtil.isOnSelectedElement(tree, x, y);
    }


    public DefaultMutableTreeNode getCurrentNode() {
        return TreeUtil.getCurrentNode(tree);
    }

    public DefaultMutableTreeNode addChild(DefaultMutableTreeNode parent, Object userObject) {
        return TreeUtil.addChild(tree, parent, userObject);
    }

    public Object getCurrentObject() {
        return TreeUtil.getCurrentObject(tree);
    }

    public <T> T getCurrentObject(Class<T> clazz) {
        return (T) TreeUtil.getCurrentObject(tree, clazz);
    }

    public void update(DefaultMutableTreeNode node) {
        DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
        model.reload(node);
    }

    public void clear() {
        DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        update(root);
    }

    public DefaultMutableTreeNode[] getCurrentNodes() {
        return TreeUtil.getCurrentNodes(tree);
    }

    public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode) tree.getModel().getRoot();
    }

    public DefaultMutableTreeNode isChildNode(DefaultMutableTreeNode parent, Object userObject) {

        for (int i = 0; i < tree.getModel().getChildCount(parent); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getChild(parent, i);
            Object o = node.getUserObject();
            if (o.equals(userObject))
                return node;
        }
        return null;
    }
    
    public DefaultMutableTreeNode[] getPathFromNodeToRoot(DefaultMutableTreeNode node){
        ArrayList<DefaultMutableTreeNode> path=new ArrayList <DefaultMutableTreeNode>();
        DefaultMutableTreeNode root=getRoot();
        while(node.getParent()!=root) {
            node= (DefaultMutableTreeNode) node.getParent();
            path.add(node);
        }
        Collections.reverse(path);
      return path.toArray(new DefaultMutableTreeNode[path.size()]);
    }
    
    public void removeNode(DefaultMutableTreeNode node){
        TreeUtil.removeNode(tree,node);
    }
    
    public static TreeNode createNode(Object ob){
    	return new DefaultMutableTreeNode(ob);
    }
    
    public static DefaultTreeModel createDefaultModel(Object rootObject){
    	return new DefaultTreeModel(createNode(rootObject));
    }
    
    public static DefaultTreeModel createDefaultModel(TreeNode root){
    	return new DefaultTreeModel(root);
    }

	public void setSingleSelection() {
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
}
