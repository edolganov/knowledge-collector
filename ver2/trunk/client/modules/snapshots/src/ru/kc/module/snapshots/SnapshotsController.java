package ru.kc.module.snapshots;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ru.kc.common.controller.Controller;
import ru.kc.common.tree.TreeNodeFacade;
import ru.kc.common.tree.TreeService;
import ru.kc.common.tree.event.GetTreeServiceRequest;
import ru.kc.components.dialog.OneTextFieldModule;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.TreeNode;
import ru.kc.module.snapshots.tools.CellRender;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.util.Check;
import ru.kc.util.swing.keyboard.DeleteKey;
import ru.kc.util.swing.keyboard.EnterKey;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class SnapshotsController extends Controller<SnapshotsPanel>{

	private static final String SNAPSHOTS_PROPERTY_KEY = "snapshots";
	
	private TreeFacade treeFacade;
	private Node owner;
	private List<SnapshotDir> snapshots = new ArrayList<SnapshotDir>();
	
	@Override
	protected void init() {
		ui.remove.setToolTipText("Delete  (Delete)");
		
		treeFacade = new TreeFacade(ui.tree);
		ui.tree.setModel(TreeFacade.createModelByUserObject(""));
		ui.tree.setRootVisible(false);
		ui.tree.setCellRenderer(new CellRender());
		treeFacade.setSingleSelection();
		
		ui.addFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createDir();
			}
		});
		ui.addSnapshot.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createSnapshot();
			}
		});
		
		ui.remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});
		ui.tree.addKeyListener(new DeleteKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				delete();
			}
			
		});
		ui.tree.addMouseListener(new MouseAdapter(){
			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Snapshot node = treeFacade.getCurrentObject(Snapshot.class);
				if(node == null) return; 
				
				if(e.getClickCount() == 2 && treeFacade.isOnSelectedElement(e.getX(), e.getY())){
					open(node);
				}
			}
			
		});
		
		ui.tree.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				Snapshot node = treeFacade.getCurrentObject(Snapshot.class);
				if(node == null) return; 
				open(node);
			}
		});
		
		
		
		
		buildTree();
	}





	private void buildTree() {
		try {
			Node root = persistTree.getRoot();
			DefaultMutableTreeNode treeRoot = treeFacade.getRoot();
			loadSnapshots(root);
			for(SnapshotDir dir : snapshots){
				DefaultMutableTreeNode dirNode = treeFacade.addChild(treeRoot, dir);
				for(Snapshot snapshot : dir.getSnapshots()){
					treeFacade.addChild(dirNode, snapshot);
				}
			}
			
			for(int i = 0; i < treeRoot.getChildCount(); ++i){
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)treeRoot.getChildAt(i);
				SnapshotDir dir = (SnapshotDir)child.getUserObject();
				if(dir.isOpen()){
					treeFacade.expand(child);
				}
			}
			
		}catch (Exception e) {
			log.error("", e);
		}
	}
	
	private void loadSnapshots(Node node) {
		List<SnapshotDir> snapshots = getSnaphots(node);
		this.snapshots = snapshots;
		this.owner = node;
	}

	private List<SnapshotDir> getSnaphots(Node node) {
		String data = node.getProperty(SNAPSHOTS_PROPERTY_KEY);
		if(data == null)
			return new ArrayList<SnapshotDir>(0);
		
		Type listType = new TypeToken<List<SnapshotDir>>(){}.getType();
		List<SnapshotDir> list = new Gson().fromJson(data, listType);
		return list;
	}
	
	private void saveSnapshots() throws Exception {
		String data = new Gson().toJson(snapshots);
		updater.update(owner, new SetProperty(SNAPSHOTS_PROPERTY_KEY, data));
		
	}
	
	
	
	private void createDir() {
		OneTextFieldModule module = new OneTextFieldModule();
		module.createDialog(rootUI, true);
		module.setTitle("Create snapshot dir");
		module.setFieldName("name");
		module.show();
		String name = module.getText();
		if(Check.isEmpty(name))
			return;
		
		try {
			createDir(name);
		} catch (Exception e) {
			log.error("", e);
		}
	}


	private DefaultMutableTreeNode createDir(String name) throws Exception {
		SnapshotDir dir = new SnapshotDir();
		dir.setName(name);
		
		DefaultMutableTreeNode beforeInsert = findBeforeInsertElement(dir);
		int insertIndex = 0;
		if(beforeInsert != null){
			insertIndex = findIndexInParent(beforeInsert) + 1;
		}
		insertDir(dir, insertIndex);
		DefaultMutableTreeNode newNode = addToTree(beforeInsert, dir);
		return newNode;
	}

	private DefaultMutableTreeNode findBeforeInsertElement(SnapshotDir dir) {
		DefaultMutableTreeNode before = getLastRootElement();
		DefaultMutableTreeNode currentNode = treeFacade.getCurrentNode();
		if(currentNode != null){
			Object ob = currentNode.getUserObject();
			if(ob instanceof Snapshot){
				currentNode = (DefaultMutableTreeNode)currentNode.getParent();
			}
			before = currentNode;
		}
		return before;
	}


	private DefaultMutableTreeNode getLastRootElement() {
		DefaultMutableTreeNode root = treeFacade.getRoot();
		int childCount = root.getChildCount();
		if(childCount == 0)
			return null;
		else 
			return (DefaultMutableTreeNode) root.getChildAt(childCount-1);
	}
	
	
	private int findIndexInParent(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		for(int i = 0; i < parent.getChildCount(); i++){
			if(parent.getChildAt(i).equals(node)){
				return i;
			}
		}
		
		return -1;
	}
	
	private void insertDir(SnapshotDir dir, int insertIndex) throws Exception {
		snapshots.add(insertIndex, dir);
		saveSnapshots();
	}
	
	private DefaultMutableTreeNode addToTree(DefaultMutableTreeNode beforeInsert, SnapshotDir dir) {
		int index = 0;
		if(beforeInsert != null){
			index = findIndexInParent(beforeInsert) + 1;
		}
		DefaultMutableTreeNode root = treeFacade.getRoot();
		DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(dir);
		root.insert(newChild, index);
		treeFacade.getModel().reload(root);
		return newChild;
		
	}
	
	
	
	
	protected void createSnapshot() {
		try {
			TreeNode root = buildTreeNodeRoot();
			if(root == null){
				return;
			}
			
			OneTextFieldModule module = new OneTextFieldModule();
			module.createDialog(rootUI, true);
			module.setTitle("Create snapshot");
			module.setFieldName("name");
			module.show();
			String name = module.getText();
			if(Check.isEmpty(name))
				return;
			
			Snapshot snapshot = new Snapshot();
			snapshot.setName(name);
			snapshot.setRoot(root);
			
			DefaultMutableTreeNode dirNode = findDirNodeToSnapshot();
			if(dirNode == null){
				dirNode = createDir("main");
			}
			SnapshotDir dir = (SnapshotDir)dirNode.getUserObject();
			dir.add(snapshot);
			saveSnapshots();
			DefaultMutableTreeNode child = treeFacade.addChild(dirNode, snapshot);
			treeFacade.setSelection(child);
			
		} catch (Exception e) {
			log.error("",e);
		}
	}
	
	private TreeNode buildTreeNodeRoot() {
		TreeService service = invokeSafe(new GetTreeServiceRequest()).result;
		if(service == null){
			return null;
		}
		
		TreeNodeFacade root = service.getRoot();
		if(! isValid(root)){
			return null;
		}
		
		TreeNode out = create(root);
		LinkedList<Info> queue = new LinkedList<Info>();
		queue.addLast(new Info(root, out));
		
		while(!queue.isEmpty()){
			Info info = queue.removeFirst();
			TreeNodeFacade treeNode = info.treeNode;
			TreeNode node = info.node;
			List<TreeNodeFacade> children = service.getChildren(treeNode);
			for(TreeNodeFacade child : children){
				if(isValid(child)){
					TreeNode treeNodeChild = create(child);
					node.addChild(treeNodeChild);
					queue.addLast(new Info(child, treeNodeChild));
				}
			}
		}
		
		return out;
	}
	
	protected void open(Snapshot snapshot) {
		TreeService service = invokeSafe(new GetTreeServiceRequest()).result;
		if(service == null){
			return;
		}
		
		TreeNode treeNodeRoot = snapshot.getRoot();
		if(treeNodeRoot == null){
			return;
		}
		
		TreeNodeFacade root = service.getRoot();
		if(isNodeContainer(root)){
			
			root.reload();
			
			LinkedList<Info> queue = new LinkedList<Info>();
			queue.addLast(new Info(root, treeNodeRoot));
			
			while(!queue.isEmpty()){
				Info info = queue.removeFirst();
				TreeNodeFacade treeNode = info.treeNode;
				treeNode.open();
				List<TreeNodeFacade> treeNodeChildren = service.getChildren(treeNode);
				
				TreeNode node = info.node;
				for(TreeNode child : node.getChildren()){
					TreeNodeFacade treeNodeChild = find(treeNodeChildren, child.getId());
					if(treeNodeChild != null){
						queue.addLast(new Info(treeNodeChild, child));
					}
				}
			}
		}
	}

	private TreeNodeFacade find(List<TreeNodeFacade> list, String id) {
		for(TreeNodeFacade treeNode : list){
			Node node = treeNode.getUserObject(Node.class);
			if(node != null){
				if(id.equals(node.getId())){
					return treeNode;
				}
			}
		}
		return null;
	}


	private boolean isValid(TreeNodeFacade node) {
		if(node.isOpen()){
			boolean nodeContainer = isNodeContainer(node);
			return nodeContainer;
		}
		
		return false;
	}
	
	private TreeNode create(TreeNodeFacade node) {
		Node ob = node.getUserObject(Node.class);
		TreeNode out = new TreeNode();
		out.setId(ob.getId());
		return out;
	}

	private boolean isNodeContainer(TreeNodeFacade node) {
		Node nodeObject = node.getUserObject(Node.class);
		if(nodeObject != null){
			return true;
		}
		return false;
	}




	private DefaultMutableTreeNode findDirNodeToSnapshot() {
		DefaultMutableTreeNode out = treeFacade.getCurrentNode();
		if(out == null){
			DefaultMutableTreeNode root = treeFacade.getRoot();
			if(root.getChildCount() == 0){
				return null;
			}
			out = (DefaultMutableTreeNode) root.getChildAt(0);
		}
		
		Object ob = out.getUserObject();
		if(ob instanceof Snapshot){
			out = (DefaultMutableTreeNode)out.getParent();
		}
		
		return out;
	}





	protected void delete() {
		Object ob = treeFacade.getCurrentObject();
		if(ob == null)
			return;
		
		try {
			if(ob instanceof SnapshotDir){
				deleteDir();
			}
			else if(ob instanceof Snapshot){
				deleteSnapshot();
			}
		}catch (Exception e) {
			log.error("", e);
		}
	}
	
	private void deleteDir() throws Exception {
		DefaultMutableTreeNode node = treeFacade.getCurrentNode();
		SnapshotDir dir = (SnapshotDir) node.getUserObject();
		boolean confirm = dialogs.confirmByDialog(rootUI, "Confirm the operation","Delete "+dir.getName()+"?");
		if(confirm){
			snapshots.remove(dir);
			saveSnapshots();
			treeFacade.removeNode(node);
		}
	}


	private void deleteSnapshot() throws Exception {
		DefaultMutableTreeNode node = treeFacade.getCurrentNode();
		Snapshot snapshot = (Snapshot) node.getUserObject();
		boolean confirm = dialogs.confirmByDialog(rootUI, "Confirm the operation","Delete "+snapshot.getName()+"?");
		if(confirm){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
			SnapshotDir dir = (SnapshotDir)parent.getUserObject();
			dir.getSnapshots().remove(snapshot);
			saveSnapshots();
			treeFacade.removeNode(node);
		}
	}






}


class Info {
	
	public final TreeNodeFacade treeNode;
	public final TreeNode node;
	
	public Info(TreeNodeFacade treeNode, TreeNode node) {
		super();
		this.treeNode = treeNode;
		this.node = node;
	}
	
}
