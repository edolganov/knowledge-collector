package ru.chapaj.util.swing.tree;

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

import ru.chapaj.util.swing.listener.KeyUpDownAdapter;

public class ExtendTree extends DNDTree {

	private static final long serialVersionUID = 1L;
	
	public static enum SelectModel{
		SINGLE
	}
	
	LinkedList<TreeNodeListener> listeners = new LinkedList<TreeNodeListener>();
	
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
						if (selRow>=0) {
							TreePath selPath = getPathForLocation(x, y);
							if(selPath != null) {
								setSelectionPath(selPath);
								menu.show( (JComponent)e.getSource(),x , y );
							}
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
		
		setDNDListener(listener);
		
		addTreeSelectionListener(new TreeSelectionListener(){

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getNewLeadSelectionPath();
				if(path == null) listener.onNodeSelect(null);
				else listener.onNodeSelect((DefaultMutableTreeNode)path.getLastPathComponent());
				
			}
			
		});
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

	public ExtendDefaultTreeModel model() {
		return getExtendModel();
	}


}
