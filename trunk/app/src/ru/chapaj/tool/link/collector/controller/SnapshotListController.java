package ru.chapaj.tool.link.collector.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import ru.chapaj.tool.link.collector.model.DataContainer;
import ru.chapaj.tool.link.collector.model.TreeSnapshot;
import ru.chapaj.tool.link.collector.model.TreeSnapshotDir;
import ru.chapaj.tool.link.collector.ui.AppFrame;
import ru.chapaj.tool.link.collector.ui.dialog.GetValueDialog;
import ru.chapaj.tool.link.collector.ui.dialog.SimpleConfirmDialog;
import ru.chapaj.util.list.ListUtil;
import ru.chapaj.util.log.Log;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.chapaj.util.swing.tree.ExtendTree.SelectModel;
import ru.chapaj.util.swing.tree.ExtendTree.TreeNodeAdapter;

public class SnapshotListController extends Controller<AppFrame> {
	
	private final static Log log = Log.getInstance(SnapshotListController.class);
	
	AppFrame ui;
	
	
	//ui model
	DefaultMutableTreeNode treeModelRoot;
	DefaultTreeCellRenderer cellRender = new DefaultTreeCellRenderer(){

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
					row, hasFocus);
			Object obj = ((DefaultMutableTreeNode)value).getUserObject();
			if(obj instanceof TreeSnapshot){
				setIcon(getLeafIcon());
			}
			else if(obj instanceof TreeSnapshotDir){
				setIcon(getClosedIcon());
			}
			else {
				setIcon(getLeafIcon());
			}
			return this;
			
		}
		
	};
	
	
	
	//app model
	DataContainer data;
	
	
	
	@Override
	public void processNewData(DataContainer data) {
		this.data = data;
		treeModelRoot.removeAllChildren();
		for(TreeSnapshot ts : data.getSnapshots()){
			treeModelRoot.add(new DefaultMutableTreeNode(ts,false));
		}
		for(TreeSnapshotDir d : data.getSnaphotDirs()){
			DefaultMutableTreeNode dir = new DefaultMutableTreeNode(d, true);
			treeModelRoot.add(dir);
			for(TreeSnapshot ts : d.getSnapshots()){
				dir.add(new DefaultMutableTreeNode(ts,false));
			}
			if(d.isOpened()){
				ui.snapTree.expandPath(dir);
			}
		}
		
		ui.snapTree.expandPath(treeModelRoot);
		ui.snapTree.updateUI();
		applaySnapshot(data.getLastTreeState());
	}
	
	@Override
	public void refreshData(DataContainer data, boolean full) {
		if(full)data.setLastTreeState(getSnapshot(null));
	}
	
	@Override
	public void init(final AppFrame ui) {
		this.ui = ui;
		ui.snapTree.init(ExtendTree.createTreeModel(null), false, cellRender, SelectModel.SINGLE);
		treeModelRoot = ui.snapTree.getRootNode();
		
		ui.snapTree.addTreeNodeListener(new TreeNodeAdapter(){

			@Override
			public void onDoubleClick(DefaultMutableTreeNode node) {
				if(node.isRoot()) return;
				Object ob = node.getUserObject();
				if(ob instanceof TreeSnapshot){
					applaySnapshot((TreeSnapshot)ob);
				}
				else if(ob instanceof TreeSnapshotDir){
					TreeSnapshotDir dir = (TreeSnapshotDir) ob;
					if(ui.snapTree.isExpanded(node)){
						dir.setOpened(true);
					}
					else dir.setOpened(false);
					//fileIsModified();
				}
			}
			
			@Override
			public void onNodeMoveDownRequest() {
				actionMoveDown();
			}
			
			@Override
			public void onNodeMoveUpRequest() {
				actionMoveUp();
			}
			
			@Override
			public void afterDrop(DefaultMutableTreeNode tagretNode,
					DefaultMutableTreeNode draggedNode) {
				actionAfterDrop(tagretNode, draggedNode);
			}
			
		});
		
		ui.snapDir.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				createDir();
			}
			
		});
		
		ui.addSnapshot.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				actionSnapshot();
			}
			
		});
		
		
		ui.deleteSnapshot.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				actionDeleteSnapshot();
			}
			
		});
		
		ui.updateB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				updateShapshot();
				
			}
			
		});
		
	}
	
	protected void updateShapshot() {
		TreeSnapshot ts = ui.snapTree.getCurrentObject(TreeSnapshot.class);
		if(ts == null) return;
		String data = TreeSnapShooter.getSnapshot(ui.tree);
		ts.setData(data);
		fileIsModified();
		
	}

	protected void actionAfterDrop(DefaultMutableTreeNode tagretNode, DefaultMutableTreeNode draggedNode) {
		TreeSnapshot ts = ui.snapTree.getUserObject(draggedNode, TreeSnapshot.class);
		if(ts == null) return;
		
		TreeSnapshotDir parent = ui.snapTree.getParentObject(draggedNode, TreeSnapshotDir.class);
		if(ui.snapTree.moveNode(tagretNode, draggedNode,TreeSnapshotDir.class)){
			if(parent == null){
				getData().getSnapshots().remove(ts);
				parent = ui.snapTree.getUserObject(tagretNode, TreeSnapshotDir.class);
				if(parent == null) parent = ui.snapTree.getParentObject(tagretNode, TreeSnapshotDir.class);
				parent.getSnapshots().add(ts);
			}
			else {
				parent.getSnapshots().remove(ts);
				getData().getSnapshots().add(ts);
			}
			fileIsModified();
		}
	}

	protected void createDir() {
		String name = GetValueDialog.getValue("Dir name");
		if(name != null){
			TreeSnapshotDir data = new TreeSnapshotDir(name);
			getData().getSnaphotDirs().add(data);
			ui.snapTree.addChild(null, data);
		}
		fileIsModified();
	}

	protected void actionMoveUp() {
		DefaultMutableTreeNode node = ui.snapTree.getCurrentNode();
		if(node == null) return;
		
		ui.snapTree.moveUpCurrentNode();
		
		Object ob = node.getUserObject();
		TreeSnapshotDir parent = ui.snapTree.getParentObject(node, TreeSnapshotDir.class);
		if(parent == null){
			if(ob instanceof TreeSnapshot){
				ListUtil.moveUp(getData().getSnapshots(),(TreeSnapshot)ob);
			}
			else if(ob instanceof TreeSnapshotDir){
				ListUtil.moveUp(getData().getSnaphotDirs(),(TreeSnapshotDir)ob);
			}
		}
		else {
			ListUtil.moveUp(parent.getSnapshots(),(TreeSnapshot)ob);
		}
		
		fileIsModified();
	}

	protected void actionMoveDown() {
		DefaultMutableTreeNode node = ui.snapTree.getCurrentNode();
		if(node == null) return;
		
		ui.snapTree.moveDownCurrentNode();
		
		Object ob = node.getUserObject();
		TreeSnapshotDir parent = ui.snapTree.getParentObject(node, TreeSnapshotDir.class);
		if(parent == null){
			if(ob instanceof TreeSnapshot){
				ListUtil.moveDown(getData().getSnapshots(),(TreeSnapshot)ob);
			}
			else if(ob instanceof TreeSnapshotDir){
				ListUtil.moveDown(getData().getSnaphotDirs(),(TreeSnapshotDir)ob);
			}
		}
		else {
			ListUtil.moveDown(parent.getSnapshots(),(TreeSnapshot)ob);
		}
		
		fileIsModified();
		
	}
	
	void actionMove(int index, int newIndex){
		//update ui
//		Object ob = listModel.remove(index);
//		listModel.add(newIndex, ob);
//		ui.snapList.setSelectedIndex(newIndex);
		
		//update app model
		List<TreeSnapshot> list = data.getSnapshots();
		TreeSnapshot ts = list.remove(index);
		list.add(newIndex, ts);
		
		fileIsModified();
	}

	protected void actionDeleteSnapshot() {
		DefaultMutableTreeNode node = ui.snapTree.getCurrentNode();
		if(node == null) return;
		Object ob = node.getUserObject();
		if(SimpleConfirmDialog.confirm("Delete snapshot", "Delete "+ob.toString()+"?")){
			if(ob instanceof TreeSnapshot){
				if(ui.snapTree.isRoot(node.getParent())){
					getData().getSnapshots().remove(ob);
				}
				else {
					ui.snapTree.getParentObject(node,TreeSnapshotDir.class).getSnapshots().remove(ob);
				}
			}
			else if(ob instanceof TreeSnapshotDir){
				getData().getSnaphotDirs().remove(ob);
			}
			ui.snapTree.removeNode(node);
			fileIsModified();
		}
	}

	void applaySnapshot(TreeSnapshot ts){
		if(ts == null || ts.getData() == null) {
			log.error("applaySnapshot: TreeSnapshot is null");
			return;
		}
		TreeSnapShooter.applaySnapshot(ui.tree, ts.getData());
	}

	public void actionSnapshot() {
		String name = GetValueDialog.getValue("Snapshot name");
		if(name != null){
			String data = TreeSnapShooter.getSnapshot(ui.tree);
			TreeSnapshot out = new TreeSnapshot(name,data);
			DefaultMutableTreeNode node = ui.snapTree.getCurrentNode();
			if(node == null || (((DefaultMutableTreeNode)node.getParent()).isRoot() && node.getUserObject() instanceof TreeSnapshot)){
				getData().getSnapshots().add(out);
				ui.snapTree.addChild(null,out,TreeSnapshotDir.class);
			}
			else {
				Object ob = node.getUserObject();
				if(ob instanceof TreeSnapshot){
					node = (DefaultMutableTreeNode)node.getParent();
					ob = node.getUserObject();
				}
				((TreeSnapshotDir) ob).getSnapshots().add(out);
				ui.snapTree.addChild(node,out);
			}
			fileIsModified();
		}
	}
	
	TreeSnapshot getSnapshot(String name){
		String data = TreeSnapShooter.getSnapshot(ui.tree);
		return new TreeSnapshot(name,data);
	}

}
