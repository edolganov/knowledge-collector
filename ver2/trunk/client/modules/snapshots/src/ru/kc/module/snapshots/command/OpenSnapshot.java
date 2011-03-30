package ru.kc.module.snapshots.command;

import java.util.LinkedList;
import java.util.List;

import ru.kc.common.command.Command;
import ru.kc.common.tree.TreeNodeFacade;
import ru.kc.common.tree.TreeService;
import ru.kc.common.tree.event.GetTreeServiceRequest;
import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.TreeNode;

public class OpenSnapshot extends Command<Void>{
	
	Snapshot snapshot;

	public OpenSnapshot(Snapshot snapshot) {
		super();
		this.snapshot = snapshot;
	}



	@Override
	protected Void invoke() throws Exception {
		open(snapshot);
		return null;
	}
	
	private void open(Snapshot snapshot) throws Exception {
		TreeService service = invoke(new GetTreeServiceRequest());
		
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


	private boolean isNodeContainer(TreeNodeFacade node) {
		Node nodeObject = node.getUserObject(Node.class);
		if(nodeObject != null){
			return true;
		}
		return false;
	}

}
