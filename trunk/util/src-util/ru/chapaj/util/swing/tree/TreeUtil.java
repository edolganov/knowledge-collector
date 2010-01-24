package ru.chapaj.util.swing.tree;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ru.chapaj.util.lang.ClassUtil;

public class TreeUtil {
	
	
	public static <T> T getCurrentObject(JTree tree, Class<T> clazz){
		DefaultMutableTreeNode curNode = getCurrentNode(tree);
		return getUserObject(curNode,clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getUserObject(DefaultMutableTreeNode node,Class<T> clazz){
		if(node == null) return null;
		Object ob = node.getUserObject();
		if(ob == null) return null;
		Class<?> objectClass = ob.getClass();
		if(!clazz.isAssignableFrom(objectClass)){
			return null;
		}
		else return (T) ob;
	}
	
	
	
	public static Object getCurrentObject(JTree tree){
		DefaultMutableTreeNode curNode = getCurrentNode(tree);
		if(curNode == null) return null;
		return curNode.getUserObject();
	}
	
	public static DefaultMutableTreeNode getCurrentNode(JTree tree){
		return (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	}

	public static DefaultMutableTreeNode addChild(JTree tree, DefaultMutableTreeNode parent, Object userObject) {
		return addChild(tree, parent, userObject, null);
	}
	
	public static DefaultMutableTreeNode addChild(JTree tree, DefaultMutableTreeNode parent, Object userObject, Class<?> downRank) {
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(userObject,true);
		addChild(tree, parent, child, downRank);
		return child;
	}
	
	public static DefaultMutableTreeNode addChild(JTree tree, DefaultMutableTreeNode parent, DefaultMutableTreeNode child, Class<?> downRank) {
		if(parent == null){
			parent = (DefaultMutableTreeNode)((DefaultTreeModel)tree.getModel()).getRoot();
		}
		
		int uiPosition = parent.getChildCount();
		if(downRank != null){
			for(int i = 0; i < parent.getChildCount(); ++i){
				Class<?> clazz = ((DefaultMutableTreeNode)parent.getChildAt(i)).getUserObject().getClass();
				if(clazz.equals(downRank) || clazz.isInstance(downRank)){
					uiPosition = i;
					break;
				}
			}
		}
		((DefaultTreeModel)tree.getModel()).insertNodeInto(child, parent, uiPosition);
		TreePath path = new TreePath(child.getPath());
		tree.setSelectionPath(path);
		tree.scrollPathToVisible(path);
		return child;
	}

	public static void expandPath(JTree tree, DefaultMutableTreeNode node) {
		tree.expandPath(new TreePath(node.getPath()));
	}

	public static void removeNode(JTree tree, DefaultMutableTreeNode node) {
		if(node == null) return;
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		model.removeNodeFromParent(node);
		
	}

	public static boolean isRoot(TreeNode node) {
		return ((DefaultMutableTreeNode)node).isRoot();
	}

	public static <T> T getParentObject(DefaultMutableTreeNode node, Class<T> clazz) {
		return getUserObject((DefaultMutableTreeNode)node.getParent(), clazz);
	}

	public static void moveDownCurrentNode(JTree tree) {
		DefaultMutableTreeNode node = TreeUtil.getCurrentNode(tree);
		if(node == null || node.isRoot()) return;
		
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		if(parent == null || parent.getChildCount() == 1) return;
		
		int position = parent.getIndex(node);
		if(position == parent.getChildCount()-1) return;
		if(!checkMovePermission(parent, node, position+1)) return;
		
		//update tree
		DefaultTreeModel model = ((DefaultTreeModel)tree.getModel());
		model.removeNodeFromParent(node);
		model.insertNodeInto(node, parent, position+1);
		tree.setSelectionPath(new TreePath(node.getPath()));
	}
	
	public static void moveUpCurrentNode(JTree tree) {
		DefaultMutableTreeNode node = TreeUtil.getCurrentNode(tree);
		if(node == null || node.isRoot()) return;
		
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		if(parent == null || parent.getChildCount() == 1) return;
		
		int position = parent.getIndex(node);
		if(position == 0) return;
		if(!checkMovePermission(parent, node, position-1)) return;
		
		//update tree
		DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
		model.removeNodeFromParent(node);
		model.insertNodeInto(node, parent, position-1);
		tree.setSelectionPath(new TreePath(node.getPath()));
	}
	
	private static boolean checkMovePermission(DefaultMutableTreeNode parent,
			DefaultMutableTreeNode node, int newPosition) {
		Class<?> a = node.getUserObject().getClass();
		Class<?> b = ((DefaultMutableTreeNode) parent.getChildAt(newPosition)).getUserObject().getClass();
		if(a.equals(b) || a.isInstance(b)){
			return true;
		}
		return false;
	}

	public static boolean moveNode(JTree tree, DefaultMutableTreeNode tagretNode, DefaultMutableTreeNode draggedNode,Class<?> validParentClass) {
		if(draggedNode.isRoot()) return false;
		if(tagretNode.equals(draggedNode)) return false;
		if(draggedNode.getParent().equals(tagretNode)) return false;
		
		if(validParentClass != null){
			Object targetObj = tagretNode.getUserObject();
			if(!ClassUtil.isValid(targetObj.getClass(), validParentClass)){
				tagretNode = (DefaultMutableTreeNode) tagretNode.getParent();
				if(tagretNode == null) return false;
				if(tagretNode.isRoot()){
					targetObj = null;
				}
				else {
					targetObj = tagretNode.getUserObject();
					if(!ClassUtil.isValid(targetObj.getClass(), validParentClass)) return false;
				}
			}
		}
		
		//проверяем что предок не перемещается в потомка
		TreeNode[] targetPath = tagretNode.getPath();
		TreeNode[] candidatPath = draggedNode.getPath();
		boolean valid = false;
		if(candidatPath.length > targetPath.length) valid = true;
		else {
			for (int i = 0; i < candidatPath.length; i++) {
				if(!candidatPath[i].equals(targetPath[i])){
					valid = true;
					break;
				}
			}
		}
		if(!valid) return false;
		

		DefaultMutableTreeNode oldParent = (DefaultMutableTreeNode)draggedNode.getParent();
		//tree
		addChild(tree, tagretNode, draggedNode, validParentClass);
		//tagretNode.add(draggedNode);
		
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		
		model.reload(oldParent);
		model.reload(tagretNode);
		
		TreePath path = new TreePath(draggedNode.getPath());
		tree.setSelectionPath(path);
		tree.scrollPathToVisible(path);
		return true;
	}

	public static boolean isExpanded(JTree tree, DefaultMutableTreeNode node) {
		return tree.isExpanded(new TreePath(node.getPath()));
	}
	
	public static boolean isOnSelectedElement(JTree tree, int x, int y){
		TreePath selPath = tree.getPathForLocation(x, y);
		if(selPath == null) return false;
		return true;
	}






}
