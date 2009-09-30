package ru.dolganov.tool.knowledge.collector.snapshot;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;
import model.tree.TreeSnapshotRoot;
import ru.chapaj.tool.link.collector.controller.TreeSnapShooter;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.chapaj.util.swing.tree.TreeNodeAdapter;
import ru.chapaj.util.swing.tree.ExtendTree.SelectModel;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;

@ControllerInfo(target=MainWindow.class)
public class SnapshotController extends Controller<MainWindow> {

	
	MainWindow ui;
	
	@Override
	public void init(MainWindow ui_) {
		ui = ui_;
		ui.snapTree.init(ExtendTree.createTreeModel(null), false, new CellRender(), SelectModel.SINGLE);
		ui.snapTree.addTreeNodeListener(new TreeNodeAdapter(){

			@Override
			public void onDoubleClick(DefaultMutableTreeNode node) {
				if(node.isRoot()) return;
				Object ob = node.getUserObject();
				if(ob == null) return;
				
				if(ob instanceof TreeSnapshot){
					applaySnapshot(((TreeSnapshot)ob));
				}
				else if(ob instanceof TreeSnapshotDir){
					TreeSnapshotDir dir = (TreeSnapshotDir) ob;
					if(ui.snapTree.isExpanded(node)){
						dir.setOpened(true);
					}
					else dir.setOpened(false);
				}
			}
			
			@Override
			public void onNodeMoveDownRequest() {
				//actionMoveDown();
			}
			
			@Override
			public void onNodeMoveUpRequest() {
				//actionMoveUp();
			}
			
			@Override
			public void afterDrop(DefaultMutableTreeNode tagretNode,
					DefaultMutableTreeNode draggedNode) {
				//actionAfterDrop(tagretNode, draggedNode);
			}
			
		});
		
		initTree();
	}

	private void initTree() {
		DefaultMutableTreeNode treeRoot = ui.snapTree.getRootNode();
		treeRoot.removeAllChildren();
		TreeSnapshotRoot treeSnapshots = dao.getRoot().getTreeSnapshots();
		if(treeSnapshots == null) return;
		List<TreeSnapshotDir> snaphotDirs = treeSnapshots.getSnaphotDirs();
		if(snaphotDirs == null) return;
		
		for(TreeSnapshotDir d : snaphotDirs){
			DefaultMutableTreeNode dir = new DefaultMutableTreeNode(d, true);
			treeRoot.add(dir);
			for(TreeSnapshot ts : d.getSnapshots()){
				dir.add(new DefaultMutableTreeNode(ts,false));
			}
			if(d.isOpened()){
				ui.snapTree.expandPath(dir);
			}
		}
		
		ui.snapTree.expandPath(treeRoot);
		ui.snapTree.updateUI();
		applaySnapshot(treeSnapshots.getLastTreeState());
		
	}

	private void applaySnapshot(TreeSnapshot ts){
		if(ts == null) return;
		TreeSnapShooter.applaySnapshot(ui.tree, ts.getData());
	}
	
	

}
