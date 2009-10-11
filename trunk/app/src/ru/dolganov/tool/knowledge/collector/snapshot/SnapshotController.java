package ru.dolganov.tool.knowledge.collector.snapshot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;
import model.tree.TreeSnapshotRoot;
import model.tree.tool.TreeSnapShooter;
import ru.chapaj.util.swing.IconHelper;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.chapaj.util.swing.tree.TreeNodeAdapter;
import ru.chapaj.util.swing.tree.ExtendTree.SelectModel;
import ru.dolganov.tool.knowledge.collector.AppUtil;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.dao.DAOEventAdapter;
import ru.dolganov.tool.knowledge.collector.dialog.DialogOps;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;
import ru.dolganov.tool.knowledge.collector.tree.TreeController;

@ControllerInfo(target=MainWindow.class,dependence=TreeController.class)
public class SnapshotController extends Controller<MainWindow> {

	
	MainWindow ui;
	
	@Override
	public void init(MainWindow ui_) {
		ui = ui_;
		ui.snapTree.init(ExtendTree.createTreeModel(null), false, new CellRender(), SelectModel.SINGLE, new TreeMenu(ui.snapTree));
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
		
		dao.addListener(new DAOEventAdapter(){
			@Override
			public void onAdded(TreeSnapshotDir dir) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(dir, true);
				ui.snapTree.getRootNode().add(node);
				ui.snapTree.updateUI();
				ui.snapTree.setSelectionPath(node);
			}
			
			@Override
			public void onAdded(TreeSnapshotDir dir, TreeSnapshot snapshot) {
				DefaultMutableTreeNode rootNode = ui.snapTree.getRootNode();
				DefaultMutableTreeNode dirNode = null;
				for(int i=0;i < rootNode.getChildCount();++i){
					DefaultMutableTreeNode c = (DefaultMutableTreeNode)rootNode.getChildAt(i);
					if(dir.equals(c.getUserObject())) {
						dirNode = c;
						break;
					}
				}
				if(dirNode != null){
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(snapshot, false);
					dirNode.add(node);
					ui.snapTree.updateUI();
					ui.snapTree.setSelectionPath(node);
				}
			}
		});
		
		ui.createSnapDirB.setText("");
		ui.createSnapDirB.setIcon(IconHelper.get("/images/kc/snap/snapDir.png"));
		ui.createSnapB.setText("");
		ui.createSnapB.setIcon(IconHelper.get("/images/kc/snap/snap.png"));
		
		ui.jButton4.setText("");
		ui.jButton5.setText("");
		
		ui.createSnapDirB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String dirName = DialogOps.newObject("New snapshot dir");
				if(dirName != null){
					TreeSnapshotDir dir = new TreeSnapshotDir();
					dir.setName(dirName);
					dao.persist(dir, null);
				}
			}
			
		});
		
		ui.createSnapB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = DialogOps.newObject("New snapshot");
				if(name != null){
					String data = TreeSnapShooter.getSnapshot(ui.tree);
					TreeSnapshot out = new TreeSnapshot(name,data);
					DefaultMutableTreeNode node = ui.snapTree.getCurrentNode();
					if(node == null || (((DefaultMutableTreeNode)node.getParent()).isRoot() && node.getUserObject() instanceof TreeSnapshot)){
						return;
					}
					else {
						Object ob = node.getUserObject();
						if(ob instanceof TreeSnapshot){
							node = (DefaultMutableTreeNode)node.getParent();
							ob = node.getUserObject();
						}
						dao.persist(out, AppUtil.map("snapshot", ((TreeSnapshotDir)ob).getName()));
					}
				}
			}
			
		});
		
		ui.jButton4.setVisible(false);
		ui.jButton5.setVisible(false);
		
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
