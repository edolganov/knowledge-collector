package ru.kc.module.snapshots.command;

import java.util.LinkedList;
import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.common.tree.TreeNodeFacade;
import ru.kc.common.tree.TreeService;
import ru.kc.common.tree.event.GetTreeServiceRequest;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.TreeNode;

public class CreateTreeNodes extends Command<TreeNode>{

	@Override
	protected TreeNode invoke() throws Exception {
		TreeNode root = buildTreeNodeRoot();
		return root;
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
