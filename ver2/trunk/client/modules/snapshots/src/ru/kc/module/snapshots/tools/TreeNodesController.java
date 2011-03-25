package ru.kc.module.snapshots.tools;

import java.util.LinkedList;
import java.util.List;

import ru.kc.common.controller.Controller;
import ru.kc.common.tree.TreeNodeFacade;
import ru.kc.common.tree.TreeService;
import ru.kc.common.tree.event.GetTreeServiceRequest;
import ru.kc.model.Node;
import ru.kc.module.snapshots.event.CreateTreeNodesRequest;
import ru.kc.module.snapshots.event.OpenSnapshotRequest;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.TreeNode;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;

@Mapping(SnapshotsPanel.class)
public class TreeNodesController extends Controller<SnapshotsPanel> {

	@Override
	protected void init() {
		
	}
	
	@EventListener
	public void createTreeNodes(CreateTreeNodesRequest request){
		TreeNode root = buildTreeNodeRoot();
		request.setResponse(root);
	}
	
	@EventListener
	public void openSnapshot(OpenSnapshotRequest request){
		Snapshot snapshot = request.snapshot;
		open(snapshot);
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
