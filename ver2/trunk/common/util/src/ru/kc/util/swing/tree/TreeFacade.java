package ru.kc.util.swing.tree;

import javax.swing.*;
import javax.swing.tree.*;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeFacade {

	public final JTree tree;
	private JPopupMenu treeMenu;
	private MenuController menuController;

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
        DefaultMutableTreeNode out = TreeUtil.addChild(tree, parent, userObject);
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        model.reload(parent);
        return out;
    }

    public Object getCurrentObject() {
        return TreeUtil.getCurrentObject(tree);
    }

    public <T> T getCurrentObject(Class<T> clazz) {
        return (T) TreeUtil.getCurrentObject(tree, clazz);
    }
    
    public <T> T getRootObject(Class<T> clazz) {
    	DefaultMutableTreeNode root = getRoot();
        return (T) TreeUtil.getUserObject(root, clazz);
    }

    public void refresh(DefaultMutableTreeNode node) {
    	DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
		if( ! node.isRoot()){
			model.reload(node);
		} else {
			TreePath selectionPath = tree.getSelectionPath();
			if(isExpanded(node)){
				collapse(node);
				expand(node);
			} else {
				expand(node);
				collapse(node);
			}
			if(selectionPath != null){
				tree.setSelectionPath(selectionPath);
			}
		}
    }

    public void clear() {
        DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        model.reload(root);
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
    
    public static DefaultMutableTreeNode createNode(Object ob){
    	return new DefaultMutableTreeNode(ob);
    }
    
    public static DefaultTreeModel createModelByUserObject(Object rootObject){
    	return new DefaultTreeModel(createNode(rootObject));
    }
    
    public static DefaultTreeModel createModelByNode(DefaultMutableTreeNode root){
    	return new DefaultTreeModel(root);
    }

	public void setSingleSelection() {
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
	
	public void setPopupMenu(JPopupMenu menu){
		setPopupMenu(menu, null);
	}
	
	
	public void setPopupMenu(JPopupMenu menu, MenuController controller){
		if(treeMenu == null){
			tree.addMouseListener(new MouseAdapter(){
				
				@Override
				public void mouseReleased(MouseEvent e) {
					if ( e.isPopupTrigger()) {
						int x = e.getX();
						int y = e.getY();
						int selRow = tree.getRowForLocation(x, y);
						if (selRow > -1) {
							TreePath selPath = tree.getPathForLocation(x, y);
							if(selPath != null) {
								tree.setSelectionPath(selPath);
								int row = tree.getRowForPath(selPath);
								Rectangle rec = tree.getRowBounds(row);
								//показываем меню в удобном месте - либо в конце ноды, либо на растоянии right
								int right = 100;
								int x_ = rec.x+rec.width > rec.x + right ? rec.x + right : rec.x + rec.width;
								if(menuController != null && menuController.canShow()){
									treeMenu.show( (JComponent)e.getSource(),x_ , rec.y + 5 );
								}

							}
						}
					}
				}
			});
			
			tree.addKeyListener(new KeyAdapter(){
				@Override
				public void keyPressed(KeyEvent e) {
					int keyCode = e.getKeyCode();
					//menu call
					if(keyCode == 525){
						DefaultMutableTreeNode currentNode = getCurrentNode();
						if(currentNode != null){
							int row = tree.getRowForPath(new TreePath(currentNode.getPath()));
							Rectangle rec = tree.getRowBounds(row);
							int right = 100;
							int x_ = rec.x+rec.width > rec.x + right ? rec.x + right : rec.x + rec.width;
							if(menuController != null && menuController.canShow()){
								treeMenu.show( (JComponent)e.getSource(),x_ , rec.y + 5 );
							}
						}
					}
				}
			});
		}
		this.treeMenu = menu;
		this.menuController = controller;
		
	}
	
	public void collapse(DefaultMutableTreeNode node){
		TreePath path =  new TreePath(node.getPath());
		tree.collapsePath(path);
	}
	
	public void expand(DefaultMutableTreeNode node) {
		tree.expandPath(new TreePath(node.getPath()));
	}
	
	public boolean isExpanded(DefaultMutableTreeNode node){
		return tree.isExpanded(new TreePath(node.getPath()));
	}
	
	public void setSelection(DefaultMutableTreeNode node){
		tree.setSelectionPath(new TreePath(node.getPath()));
	}
	

	
	public boolean isSelectedNode(DefaultMutableTreeNode node){
		DefaultMutableTreeNode selected = getCurrentNode();
		if(selected != null && selected.equals(node)){
			return true;
		}
		return false;
	}
	
	
	public boolean moveNode(DefaultMutableTreeNode tagretNode, DefaultMutableTreeNode draggedNode){
		return TreeUtil.moveNode(tree, tagretNode, draggedNode, false);
	}

	public DefaultTreeModel getModel() {
		return (DefaultTreeModel) tree.getModel();
		
	}

	public List<DefaultMutableTreeNode> getChildren(DefaultMutableTreeNode node) {
		ArrayList<DefaultMutableTreeNode> out = new ArrayList<DefaultMutableTreeNode>();
		for(int i=0; i < node.getChildCount(); i++){
			out.add((DefaultMutableTreeNode)node.getChildAt(i));
		}
		return out;
	}

	public DefaultMutableTreeNode getParentOfCurrentNode() {
		DefaultMutableTreeNode node = getCurrentNode();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		return parent;
		
	}
	
	public void moveChild(DefaultMutableTreeNode childNode, int newIndex){
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)childNode.getParent();
		if(parentNode == null)
			return;
		
		TreePath childPath = new TreePath(childNode.getPath());
		TreePath selectedPath = null;
		if(tree.isPathSelected(childPath)){
			selectedPath = childPath;
		}
		
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			DefaultMutableTreeNode candidat = (DefaultMutableTreeNode)parentNode.getChildAt(i);
			if(candidat == childNode){
				parentNode.remove(childNode);
				parentNode.insert(childNode, newIndex);
			}					
		}
		getModel().reload(parentNode);
		
		if(selectedPath != null) tree.setSelectionPath(selectedPath);
		
	}
}
