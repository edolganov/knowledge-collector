package ru.kc.module.imports.oldclient.oldmodel;

import java.util.LinkedList;
import java.util.List;

import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.TreeNode;
import ru.kc.util.UuidGenerator;


public class OldSnapshotConverter {
	
	private static char SEPARATOR = ';';
	private static char BACK_CHAR = 'r';
	
	private String idPreffix = "";
	
	
	
	public OldSnapshotConverter(String idPreffix) {
		super();
		this.idPreffix = idPreffix;
	}


	public Snapshot convert(Node root, TreeSnapshot oldSnapshot) throws Exception {
		
		String name = oldSnapshot.getName();
		String data = oldSnapshot.getData();
		
		TreeNode rootNode = new TreeNode();
		rootNode.setId(root.getId());
		
		//парсинг даты
		//новый формат: 0;3829873523;r233239840932;209384098324;4345435345;35345345;rrrr345435435345;r
		//где 0;3829873523; - пустышка

		StringBuilder curOp = new StringBuilder();
		char c;
		TreeNode curNode = rootNode;
		TreeNode node;
		LinkedList<TreeNode> parentSrack = new LinkedList<TreeNode>();
		for(int i=2; i < data.length(); ++i){
			c = data.charAt(i);
			if(c == SEPARATOR) {
				String op = curOp.toString();
				node = createChild(op, curNode);
				parentSrack.addLast(curNode);
				curNode = node;
				curOp = new StringBuilder();
			}
			else if(c == BACK_CHAR){
				curNode = parentSrack.removeLast();
			}
			else {
				curOp.append(c);
			}
		}
		
		//удаляем пустышку
		List<TreeNode> children = rootNode.getChildren();
		if(children.size() != 1){
			throw new IllegalStateException("invalid data");
		}
		TreeNode oldRoot = children.get(0);
		rootNode.setChildren(oldRoot.getChildren());
		
		Snapshot snapshot = new Snapshot();
		snapshot.setId(UuidGenerator.simpleUuid());
		snapshot.setName(name);
		snapshot.setRoot(rootNode);
		
		return snapshot;
	}
	

	private TreeNode createChild(String curOp, TreeNode parent) {
		String id = idPreffix+curOp;
		TreeNode child = new TreeNode();
		child.setId(id);
		parent.getChildren().add(child);
		return child;
	}

}
