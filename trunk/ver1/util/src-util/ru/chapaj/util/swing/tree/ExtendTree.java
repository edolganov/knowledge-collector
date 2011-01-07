package ru.chapaj.util.swing.tree;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ru.chapaj.util.lang.ClassUtil;
import ru.chapaj.util.swing.listener.KeyUpDownAdapter;

public class ExtendTree extends DNDTree {

	private static final long serialVersionUID = 1L;
	
	public static enum SelectModel{
		SINGLE
	}
	
	LinkedList<TreeNodeListener> listeners = new LinkedList<TreeNodeListener>();
	boolean isEnabledDefaultMoveOperations = false;
	
	public ExtendTree() {
		super();
		initAfterSuper();
	}

	private void initAfterSuper() {
		addKeyListener(new KeyUpDownAdapter(){

			@Override
			public void moveDown() {
				//System.out.println("listener.onNodeMoveDownRequest()");
				for(TreeNodeListener l : listeners)
				l.onNodeMoveDownRequest();
				
			}

			@Override
			public void moveUp() {
				for(TreeNodeListener l : listeners)
				l.onNodeMoveUpRequest();
				
			}
			
		});
		
	}

	public ExtendTree(boolean autoUpdateModel) {
		super(autoUpdateModel);
		initAfterSuper();
	}

	public ExtendTree(ExtendDefaultTreeModel model, boolean autoUpdateModel) {
		super(model, autoUpdateModel);
		initAfterSuper();
	}
	
	public ExtendDefaultTreeModel getExtendModel() {
		return (ExtendDefaultTreeModel)super.getModel();
	}
	
	public void init(ExtendDefaultTreeModel model,
			boolean setRootVisible,
			TreeCellRenderer cellRender,
			SelectModel selectModel){
		init(model, setRootVisible, cellRender, selectModel, null);
	}
	
	public void init(ExtendDefaultTreeModel model,
			boolean setRootVisible,
			TreeCellRenderer cellRender,
			SelectModel selectModel,
			final JPopupMenu menu){
		if(model != null)setModel(model);
		setRootVisible(setRootVisible);
		if(cellRender != null)setCellRenderer(cellRender);
		if(selectModel != null){
			if(SelectModel.SINGLE == selectModel) getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}
		if(menu != null){
			
			addMouseListener(new MouseAdapter(){
				
				@Override
				public void mouseReleased(MouseEvent e) {
					if ( e.isPopupTrigger()) {
						int x = e.getX();
						int y = e.getY();
						int selRow = getRowForLocation(x, y);
						if (selRow > -1) {
							TreePath selPath = getPathForLocation(x, y);
							if(selPath != null) {
								setSelectionPath(selPath);
								int row = getRowForPath(selPath);
								Rectangle rec = getRowBounds(row);
								//показываем меню в удобном месте - либо в конце ноды, либо на растоянии right
								int right = 100;
								int x_ = rec.x+rec.width > rec.x + right ? rec.x + right : rec.x + rec.width;
								menu.show( (JComponent)e.getSource(),x_ , rec.y + 5 );
								//menu.show( (JComponent)e.getSource(),x , y );
							}
						}
					}
				}
			});
			
			addKeyListener(new KeyAdapter(){
				@Override
				public void keyPressed(KeyEvent e) {
					int keyCode = e.getKeyCode();
					//menu call
					if(keyCode == 525){
						DefaultMutableTreeNode currentNode = getCurrentNode();
						if(currentNode != null){
							int row = getRowForPath(new TreePath(currentNode.getPath()));
							Rectangle rec = getRowBounds(row);
							int right = 100;
							int x_ = rec.x+rec.width > rec.x + right ? rec.x + right : rec.x + rec.width;
							menu.show( (JComponent)e.getSource(),x_ , rec.y + 5 );
						}
					}
				}
			});
		}
	}
	
	public static ExtendDefaultTreeModel createTreeModel(Object rootObject){
		DefaultMutableTreeNode treeModelRoot = new DefaultMutableTreeNode(rootObject);
		return new ExtendDefaultTreeModel(treeModelRoot, false);
	}
	
	public void addTreeNodeListener(final TreeNodeListener listener){
		listeners.add(listener);
		
		addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseReleased(MouseEvent e) {
//				if ( e.isPopupTrigger()) {
//					DefaultMutableTreeNode node = getCurrentSelectedNode();
//					if(node != null && AppUtil.isLink(node.getUserObject())){
//						popup.show( (JComponent)e.getSource(), e.getX(), e.getY() );
//					}
//				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
					if(node == null) return;
					listener.onDoubleClick(node);
				}
			}
			
		});
		
		addDNDListener(listener);
		
		addTreeSelectionListener(new TreeSelectionListener(){

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getNewLeadSelectionPath();
				if(path == null) listener.onNodeSelect(null);
				else listener.onNodeSelect((DefaultMutableTreeNode)path.getLastPathComponent());
				
			}
			
		});
	}
	
	
	public synchronized void enableDefaultMoveOperations(final MoveNodeListener listener){
		if(!isEnabledDefaultMoveOperations){
			isEnabledDefaultMoveOperations = true;
			addTreeNodeListener(new TreeNodeAdapter(){
				@Override
				public void onNodeMoveDownRequest() {
					moveNode(true,listener);
				}
				
				public void onNodeMoveUpRequest() {
					moveNode(false,listener);
				}
			});
		}
	}
	
	private void moveNode(boolean down, MoveNodeListener listener) {
		DefaultMutableTreeNode node = getCurrentNode();
		if(node == null) return;
		Object userObject = node.getUserObject();
		if(userObject == null) return;
		
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		if(parent == null || parent.getChildCount() == 1) return;
		
		int childCount = parent.getChildCount();

		int oldIndex = parent.getIndex(node);
		int minIndex = oldIndex;

		Class<?> validClass = userObject.getClass();
		for(int i = oldIndex -1 ; i > -1; i--){
			minIndex = i;
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)parent.getChildAt(i);
			Object ob = child.getUserObject();
			if(ob == null || !ClassUtil.isValid(ob.getClass(), validClass)){
				++minIndex;
				break;
			}
		}
		int maxIndex = oldIndex;
		for(int i = oldIndex +1 ; i < childCount; i++){
			maxIndex = i;
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)parent.getChildAt(i);
			Object ob = child.getUserObject();
			if(ob == null || !ClassUtil.isValid(ob.getClass(), validClass)){
				--maxIndex;
				break;
			}
		}
		if(minIndex == maxIndex) return;
		int newIndex = 0;
		if(down) newIndex = oldIndex + 1;
		else newIndex = oldIndex - 1;
		if(newIndex > maxIndex) newIndex = minIndex;
		if(newIndex < minIndex) newIndex = maxIndex;
		//System.out.println("oldIndex:"+oldIndex + " newIndex:" + newIndex);
		//update model
		if(listener.onNodeMoveRequest(newIndex)){
			//update tree
			ExtendDefaultTreeModel model = model();
			model.removeNodeFromParent(node);
			model.insertNodeInto(node, parent, newIndex);
			TreePath treePath = new TreePath(node.getPath());
			setSelectionPath(treePath);
		}
	}
	
	public void moveNode(DefaultMutableTreeNode childNode, DefaultMutableTreeNode parentNode){
		DefaultMutableTreeNode oldParent = (DefaultMutableTreeNode)childNode.getParent();
		parentNode.add(childNode);
		model().reload(oldParent);
		model().reload(parentNode);
		TreePath path = new TreePath(childNode.getPath());
		setSelectionPath(path);
		scrollPathToVisible(path);
	}
	
	public boolean moveNode(DefaultMutableTreeNode tagretNode, DefaultMutableTreeNode draggedNode,Class<?> validParentClass){
		return TreeUtil.moveNode(this, tagretNode, draggedNode, validParentClass);
	}
	
	public <T> T getCurrentObject(Class<T> clazz){
		return TreeUtil.getCurrentObject(this, clazz);
	}
	
	public <T> T getUserObject(DefaultMutableTreeNode node,Class<T> clazz){
		return TreeUtil.getUserObject(node, clazz);
	}
	
	public Object getCurrentObject(){
		return TreeUtil.getCurrentObject(this);
	}
	
	public DefaultMutableTreeNode getCurrentNode(){
		return TreeUtil.getCurrentNode(this);
	}
	
	public DefaultMutableTreeNode addChild(DefaultMutableTreeNode parent, Object userObject) {
		return TreeUtil.addChild(this, parent, userObject);
	}
	
	
	public DefaultMutableTreeNode addChild(DefaultMutableTreeNode parent, Object userObject, Class<?> downRank) {
		return TreeUtil.addChild(this, parent, userObject, downRank);
	}
	
	public DefaultMutableTreeNode addChild(DefaultMutableTreeNode parent, DefaultMutableTreeNode child) {
		return addChild(parent, child, null);
	}
	
	public DefaultMutableTreeNode addChild(DefaultMutableTreeNode parent, DefaultMutableTreeNode child, Class<?> downRank) {
		return TreeUtil.addChild(this, parent, child, downRank);
	}

	public void expandPath(DefaultMutableTreeNode node) {
		TreeUtil.expandPath(this, node);
	}

	public void removeNode(DefaultMutableTreeNode node) {
		TreeUtil.removeNode(this, node);
		
	}

	public boolean isRoot(TreeNode node) {
		return TreeUtil.isRoot(node);
	}

	public <T> T getParentObject(DefaultMutableTreeNode node, Class<T> clazz) {
		return TreeUtil.getParentObject(node, clazz);
	}
	
	public <T> T getParentObject(Class<T> clazz) {
		return getParentObject(getCurrentNode(), clazz);
	}

	public void moveDownCurrentNode() {
		TreeUtil.moveDownCurrentNode(this);
	}
	
	public void moveUpCurrentNode() {
		TreeUtil.moveUpCurrentNode(this);
	}

	public boolean isExpanded(DefaultMutableTreeNode node) {
		return TreeUtil.isExpanded(this,node);
	}

	public DefaultMutableTreeNode getRootNode() {
		return (DefaultMutableTreeNode)treeModel.getRoot();
	}

	public void removeAllChildren() {
		getRootNode().removeAllChildren();
		
	}

	public void setRoot(DefaultMutableTreeNode root) {
		getExtendModel().setRoot(root);
		
	}
	
    public void setSelectionPath(DefaultMutableTreeNode node) {
        getSelectionModel().setSelectionPath(new TreePath(node.getPath()));
    }

	public ExtendDefaultTreeModel model() {
		return getExtendModel();
	}


}
