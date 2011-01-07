package ru.chapaj.tool.link.collector.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import model.tree.TreeSnapshot;

import ru.chapaj.tool.link.collector.App;
import ru.chapaj.tool.link.collector.AppUtil;
import ru.chapaj.tool.link.collector.command.OpenLinkByDefault;
import ru.chapaj.tool.link.collector.model.DataContainer;
import ru.chapaj.tool.link.collector.model.Dir;
import ru.chapaj.tool.link.collector.model.Link;
import ru.chapaj.tool.link.collector.model.NodeMeta;
import ru.chapaj.tool.link.collector.ui.AppFrame;
import ru.chapaj.tool.link.collector.ui.dialog.GetValueDialog;
import ru.chapaj.tool.link.collector.ui.dialog.NewDirDialog;
import ru.chapaj.tool.link.collector.ui.dialog.NewLinkDialog;
import ru.chapaj.tool.link.collector.ui.dialog.SimpleConfirmDialog;
import ru.chapaj.util.log.Log;
import ru.chapaj.util.swing.listener.KeyUpDownAdapter;
import ru.chapaj.util.swing.tree.DNDTree.DNDListener;

public class TreeController extends Controller<AppFrame> {
	
	private static final Log log = Log.getInstance(TreeController.class);
	
	AppFrame ui;
	
	@Override
	public void init(final AppFrame ui) {
		this.ui = ui;
		ui.tree.setCellRenderer(new CustomIconRenderer());
		ui.newDir.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				actionCreateDir();
			}
			
		});
		
		ui.newLink.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				actionCreateLink();
			}
			
		});
		
		ui.tree.addTreeSelectionListener(new TreeSelectionListener(){

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getNewLeadSelectionPath();
				if(path == null) App.getDefault().getNodeInfoController().hide();
				else {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
					if(!node.isRoot()){
						App.getDefault().getNodeInfoController().info(node.getUserObject());
					}
					else {
						App.getDefault().getNodeInfoController().hide();
					}
				}
			}
				
			
		});
		
		ui.tree.addDNDListener(new DNDListener(){

			@Override
			public void afterDrop(DefaultMutableTreeNode tagretNode,
					DefaultMutableTreeNode draggedNode) {
				actionAfterDrop(tagretNode, draggedNode);
			}
			
		});
		
		final JPopupMenu popup = new JPopupMenu();
		new LinkPopupMenuController().init(popup);
		
		ui.tree.addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if ( e.isPopupTrigger()) {
					DefaultMutableTreeNode node = getCurrentSelectedNode();
					if(node != null && AppUtil.isLink(node.getUserObject())){
						popup.show( (JComponent)e.getSource(), e.getX(), e.getY() );
					}
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					//handle double click. 
					Object obj = getCurrentTreeObject();
					if(AppUtil.isLink(obj)){
						new OpenLinkByDefault((Link)obj).invoke();
					}
				}
			}
			
			
		});
		
		
		ui.tree.addKeyListener(new KeyUpDownAdapter(){

			@Override
			public void moveDown() {
				actionMoveNodeDown();
			}

			@Override
			public void moveUp() {
				actionMoveNodeUp();
			}
			
		});
		
		
		
	}
	
	protected void actionMoveNodeDown() {
		DefaultMutableTreeNode node = getCurrentSelectedNode();
		if(node == null || node.isRoot()) return;
		
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		if(parent == null || parent.getChildCount() == 1) return;
		if(! AppUtil.isDir(parent.getUserObject())) {
			log.error("actionMoveNodeDown(): parent is not dir");
			return;
		}
		
		int position = parent.getIndex(node);
		if(position == parent.getChildCount()-1) return;
		if(!checkMovePermission(parent, node, position+1)) return;
		
		//update tree
		DefaultTreeModel model = ((DefaultTreeModel)ui.tree.getModel());
		model.removeNodeFromParent(node);
		model.insertNodeInto(node, parent, position+1);
		ui.tree.setSelectionPath(new TreePath(node.getPath()));
		
		//update model
		((Dir)parent.getUserObject()).moveDown(node.getUserObject());
		fileIsModified();
		
	}

	private boolean checkMovePermission(DefaultMutableTreeNode parent,
			DefaultMutableTreeNode node, int newPosition) {
		Class<?> a = node.getUserObject().getClass();
		Class<?> b = ((DefaultMutableTreeNode) parent.getChildAt(newPosition)).getUserObject().getClass();
		if(a.equals(b) || a.isInstance(b)){
			return true;
		}
		return false;
		
	}

	protected void actionMoveNodeUp() {
		DefaultMutableTreeNode node = getCurrentSelectedNode();
		if(node == null || node.isRoot()) return;
		
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		if(parent == null || parent.getChildCount() == 1) return;
		if(! AppUtil.isDir(parent.getUserObject())) {
			log.error("actionMoveNodeUp(): parent is not dir");
			return;
		}
		
		int position = parent.getIndex(node);
		if(position == 0) return;
		if(!checkMovePermission(parent, node, position-1)) return;
		
		//update tree
		DefaultTreeModel model = ((DefaultTreeModel)ui.tree.getModel());
		model.removeNodeFromParent(node);
		model.insertNodeInto(node, parent, position-1);
		ui.tree.setSelectionPath(new TreePath(node.getPath()));
		
		//update model
		((Dir)parent.getUserObject()).moveUp(node.getUserObject());
		fileIsModified();
		
//		System.out.println(Thread.currentThread().getStackTrace()[1]
//				.getMethodName());
		
	}

	private void actionAfterDrop(DefaultMutableTreeNode tagretNode,
			DefaultMutableTreeNode draggedNode){
		if(draggedNode.isRoot()) return;
		if(tagretNode.equals(draggedNode)) return;
		//перемещаем только в папку
		Object targetObj = tagretNode.getUserObject();
		if(! AppUtil.isDir(targetObj)){
			tagretNode = (DefaultMutableTreeNode) tagretNode.getParent();
			if(tagretNode == null) {
				// [04.05.2009] chapaj: корневая нода была не папкой
				return;
			}
			else targetObj = tagretNode.getUserObject();
		}
		if(!AppUtil.isDir(targetObj)) return;
		if(draggedNode.getParent().equals(tagretNode)) return;
		
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
		if(valid){
			DefaultMutableTreeNode oldParent = (DefaultMutableTreeNode)draggedNode.getParent();
			//tree
			tagretNode.add(draggedNode);
			
			//model
			Object childObj = draggedNode.getUserObject();
			((Dir)oldParent.getUserObject()).remove(childObj);
			((Dir)targetObj).addChild(childObj);
			
			DefaultTreeModel model = (DefaultTreeModel)ui.tree.getModel();
			
			model.reload(oldParent);
			model.reload(tagretNode);
			
			TreePath path = new TreePath(draggedNode.getPath());
			ui.tree.setSelectionPath(path);
			ui.tree.scrollPathToVisible(path);
			
			fileIsModified();
		}
	}

	public void actionCreateDir(){
		Dir dir = NewDirDialog.getDir();
		if(dir != null) createDir(dir);
	}
	
	public void actionCreateLink(){
		Link link = NewLinkDialog.getLink();
		if(link != null) createLink(link);
	}
	
	public void actionDeleteNode(){
		DefaultMutableTreeNode node = getCurrentSelectedNode();
		if(node != null && ! node.isRoot()) {
			if(SimpleConfirmDialog.confirm("Delete node", "Delete node and all his subnode?")){
				deleteNode(node);
			}
		}
	}

	protected void deleteNode(DefaultMutableTreeNode node) {
		JTree tree = ui.tree;
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		if(parent.getUserObject() instanceof Dir){
			model.removeNodeFromParent(node);
			((Dir)parent.getUserObject()).remove(node.getUserObject());
		}
		fileIsModified();
	}

	protected void createLink(Link link) {
		JTree tree = ui.tree;
		DefaultMutableTreeNode parent = getCurrentSelectedNode();
		if(parent == null) parent = getTreeRoot();
		if(! AppUtil.isDir(parent.getUserObject())){
			parent = (DefaultMutableTreeNode) parent.getParent();
		}
		if(parent == null || ! AppUtil.isDir(parent.getUserObject())) return;
		
		((Dir)parent.getUserObject()).addChild(link);
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(link,false);
		((DefaultTreeModel)tree.getModel()).insertNodeInto(child, parent, parent.getChildCount());
		fileIsModified();
		
	}

	protected void createDir(Dir dir) {
		JTree tree = ui.tree;
		DefaultMutableTreeNode parent = getCurrentSelectedNode();
		if(parent == null) parent = getTreeRoot();
		if(! AppUtil.isDir(parent.getUserObject())){
			parent = (DefaultMutableTreeNode) parent.getParent();
		}
		if(parent == null || ! AppUtil.isDir(parent.getUserObject())) return;
		
		((Dir)parent.getUserObject()).addChild(dir);
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(dir,true);
		int uiPosition = parent.getChildCount();
		for(int i = 0; i < parent.getChildCount(); ++i){
			if(!AppUtil.isDir(((DefaultMutableTreeNode)parent.getChildAt(i)).getUserObject())){
				uiPosition = i;
				break;
			}
		}
		((DefaultTreeModel)tree.getModel()).insertNodeInto(child, parent, uiPosition);
		
		TreePath path = new TreePath(child.getPath());
		tree.setSelectionPath(path);
		tree.scrollPathToVisible(path);
		fileIsModified();
		
	}

	public void processNewData(DataContainer data) {
		DefaultTreeModel model = (DefaultTreeModel)ui.tree.getModel();
		DefaultMutableTreeNode root = getTreeRoot();
		for(int i = model.getChildCount(root)-1; i > -1;--i){
			model.removeNodeFromParent((MutableTreeNode)model.getChild(root, i));
		}
		Dir newRootDir = data.getRoot();
		root.setUserObject(newRootDir);
		fillTree(root,newRootDir.getSubDir(),newRootDir.getLinks());
		
	}
	
	public static class StepInfo {
		DefaultMutableTreeNode node;
		ArrayList<Dir> subDir;
		ArrayList<Link> links;
		public StepInfo(DefaultMutableTreeNode node, ArrayList<Dir> subDir,
				ArrayList<Link> links) {
			super();
			this.node = node;
			this.subDir = subDir;
			this.links = links;
		}
	}

	private void fillTree(DefaultMutableTreeNode node,ArrayList<Dir> subDir, ArrayList<Link> links) {
		if(! (node.getUserObject() instanceof Dir)) return;
		JTree tree = ui.tree;
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		
		LinkedList<StepInfo> queue = new LinkedList<StepInfo>();
		queue.addLast(new StepInfo(node,subDir,links));
		
		StepInfo step;
//		ArrayList<DefaultMutableTreeNode> children
		while(!queue.isEmpty()){
			step = queue.removeFirst();
			if(step.subDir != null){
				for(Dir dir : step.subDir){
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(dir,true);
					model.insertNodeInto(child, step.node, step.node.getChildCount());
					if(dir.getSubDir() != null || dir.getLinks() != null){
						queue.addLast(new StepInfo(child,dir.getSubDir(),dir.getLinks()));
					}
				}
			}
			if(step.links != null){
				for(Link link : step.links){
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(link,false);
					model.insertNodeInto(child, step.node, step.node.getChildCount());
				}
			}
		}
		
	}

	public void updateCurNode(String... data) {
		String name = data[0];
		String url = data[1];
		String description = data[2];
		
		JTree tree = ui.tree;
		DefaultMutableTreeNode curNode = getCurrentSelectedNode();
		if(curNode == null || curNode.isRoot()) return;
		Object obj = curNode.getUserObject();
		if(AppUtil.isDir(obj)){
			if(name == null || name.length() == 0) return;
			NodeMeta meta = ((Dir) obj).getMeta();
			meta.setName(name);
			meta.setDescription(description);
			
		}
		else if(AppUtil.isLink(obj)){
			if(url == null || url.length() == 0) return;
			if(name == null || name.length() == 0) name = new String(url);
			Link link = (Link)obj;
			link.setName(name);
			link.setUrl(url);
			link.setDescription(description);
		}
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		model.reload(curNode);
		fileIsModified();
	}
	
	private DefaultMutableTreeNode getTreeRoot(){
		return (DefaultMutableTreeNode)ui.tree.getModel().getRoot();
	}
	
	private DefaultMutableTreeNode getCurrentSelectedNode(){
		return (DefaultMutableTreeNode) ui.tree.getLastSelectedPathComponent();
	}

	public Object getCurrentTreeObject() {
		DefaultMutableTreeNode curNode = getCurrentSelectedNode();
		if(curNode == null) return null;
		return curNode.getUserObject();
	}

}
