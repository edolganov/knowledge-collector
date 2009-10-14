package ru.dolganov.tool.knowledge.collector.snapshot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.Root;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;
import model.tree.TreeSnapshotRoot;
import model.tree.tool.TreeSnapShooter;
import ru.chapaj.util.collection.ListUtil;
import ru.chapaj.util.swing.IconHelper;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.chapaj.util.swing.tree.MoveNodeListener;
import ru.chapaj.util.swing.tree.TreeNodeAdapter;
import ru.chapaj.util.swing.tree.ExtendTree.SelectModel;
import ru.dolganov.tool.knowledge.collector.App;
import ru.dolganov.tool.knowledge.collector.AppListener;
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
	ExtendTree tree;
	
	@Override
	public void init(MainWindow ui_) {
		ui = ui_;
		tree = ui.snapTree;
		tree.init(ExtendTree.createTreeModel(null), false, new CellRender(), SelectModel.SINGLE, new TreeMenu(tree,this));
		tree.addTreeNodeListener(new TreeNodeAdapter(){

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
					if(tree.isExpanded(node)){
						dir.setOpened(true);
					}
					else dir.setOpened(false);
				}
			}
			
			@Override
			public void afterDrop(DefaultMutableTreeNode tagretNode,
					DefaultMutableTreeNode draggedNode) {
				Object ob = draggedNode.getUserObject();
				if(! (ob instanceof TreeSnapshot)) return;
				TreeSnapshot snap = (TreeSnapshot) ob;
				
				Object oldDirOb = ((DefaultMutableTreeNode)draggedNode.getParent()).getUserObject();
				if(!(oldDirOb instanceof TreeSnapshotDir)) return;
				TreeSnapshotDir oldDir = (TreeSnapshotDir) oldDirOb;
				
				
				Object tOb = tagretNode.getUserObject();
				TreeSnapshotDir dir = null;
				if(tOb instanceof TreeSnapshotDir){
					dir = (TreeSnapshotDir)tOb;
				}
				else {
					tOb = ((DefaultMutableTreeNode)tagretNode.getParent()).getUserObject();
					if(tOb instanceof TreeSnapshotDir) {
						dir = (TreeSnapshotDir)tOb;
						tagretNode = (DefaultMutableTreeNode)tagretNode.getParent();
					}
				}
				if(dir == null) return;
				
				dir.getSnapshots().add(snap);
				oldDir.getSnapshots().remove(snap);
				dao.merge(dao.getRoot());
				tree.moveNode(draggedNode, tagretNode);
				
			}
			
		});
		
		tree.enableDefaultMoveOperations(new MoveNodeListener(){

			@Override
			public boolean onNodeMoveRequest(int newIndex) {
				Object ob = tree.getCurrentObject();
				if(ob == null) return false;
				
				if(ob instanceof TreeSnapshot){
					TreeSnapshotDir dir = tree.getParentObject(TreeSnapshotDir.class);
					ListUtil.move(dir.getSnapshots(), (TreeSnapshot)ob, newIndex);
					Root root = dao.getRoot();
					dao.merge(root);
					return true;
				}
				else if(ob instanceof TreeSnapshotDir){
					Root root = dao.getRoot();
					ListUtil.move(root.getTreeSnapshots().getSnaphotDirs(),(TreeSnapshotDir)ob,newIndex);
					dao.merge(root);
					return true;
				}
				return false;
			}
			
		});
		
		dao.addListener(new DAOEventAdapter(){
			@Override
			public void onAdded(TreeSnapshotDir dir) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(dir, true);
				tree.getRootNode().add(node);
				tree.updateUI();
				tree.setSelectionPath(node);
			}
			
			@Override
			public void onAdded(TreeSnapshotDir dir, TreeSnapshot snapshot) {
				DefaultMutableTreeNode dirNode = findDirNode(dir);
				if(dirNode != null){
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(snapshot, false);
					dirNode.add(node);
					tree.updateUI();
					tree.setSelectionPath(node);
				}
			}
			
			@Override
			public void onDeleted(TreeSnapshot snapshot) {
				DefaultMutableTreeNode node = tree.getCurrentNode();
				if(node == null || snapshot != node.getUserObject()) return;
				tree.model().removeNodeFromParent(node);
				tree.updateUI();
			}
			
			@Override
			public void onDeleted(TreeSnapshotDir snapshot) {
				DefaultMutableTreeNode node = tree.getCurrentNode();
				if(node == null || snapshot != node.getUserObject()) return;
				tree.model().removeNodeFromParent(node);
				tree.updateUI();
			}
		});
		
		App.getDefault().addListener(new AppListener(){

			@Override
			public void onAction(Object source, String action, Object... data_) {
				if("exit".equals(action)){
					String data = TreeSnapShooter.getSnapshot(ui.tree);
					if(data != null){
						Root root = dao.getRoot();
						root.getTreeSnapshots().getLastTreeState().setData(data);
						dao.merge(root,true);
					}
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
					dao.add(dir);
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
					DefaultMutableTreeNode node = tree.getCurrentNode();
					if(node == null || (((DefaultMutableTreeNode)node.getParent()).isRoot() && node.getUserObject() instanceof TreeSnapshot)){
						return;
					}
					else {
						Object ob = node.getUserObject();
						if(ob instanceof TreeSnapshot){
							node = (DefaultMutableTreeNode)node.getParent();
							ob = node.getUserObject();
						}
						dao.add(out, AppUtil.map("snapshot", ((TreeSnapshotDir)ob).getName()));
					}
				}
			}
			
		});
		
		ui.jButton4.setVisible(false);
		ui.jButton5.setVisible(false);
		
		initTree();
	}

	private void initTree() {
		DefaultMutableTreeNode treeRoot = tree.getRootNode();
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
				tree.expandPath(dir);
			}
		}
		
		tree.expandPath(treeRoot);
		tree.updateUI();
		applaySnapshot(treeSnapshots.getLastTreeState());
		
	}

	private void applaySnapshot(TreeSnapshot ts){
		if(ts == null) return;
		TreeSnapShooter.applaySnapshot(ui.tree, ts.getData());
	}

	public void deleteCurrentElement() {
		Object ob = tree.getCurrentObject();
		if(ob == null) return;
		if(ob instanceof TreeSnapshot) {
			TreeSnapshotDir dir = tree.getParentObject(tree.getCurrentNode(), TreeSnapshotDir.class);
			if(dir != null) dao.delete(dir, (TreeSnapshot)ob);
		}
		else if(ob instanceof TreeSnapshotDir){
			dao.delete((TreeSnapshotDir)ob);
		}
		
	}
	
	private DefaultMutableTreeNode findDirNode(TreeSnapshotDir dir) {
		DefaultMutableTreeNode rootNode = tree.getRootNode();
		DefaultMutableTreeNode dirNode = null;
		for(int i=0;i < rootNode.getChildCount();++i){
			DefaultMutableTreeNode c = (DefaultMutableTreeNode)rootNode.getChildAt(i);
			if(dir.equals(c.getUserObject())) {
				dirNode = c;
				break;
			}
		}
		return dirNode;
	}

	public void resnapCurrentSnapshot() {
		TreeSnapshot snap = tree.getCurrentObject(TreeSnapshot.class);
		if(snap != null){
			String data = TreeSnapShooter.getSnapshot(ui.tree);
			if(data != null)snap.setData(data);
			dao.update(snap);
		}
	}
	
	

}
