package model.tree.tool;

import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import model.HavingUuid;


public class TreeSnapShooter {
	
	private static class StepInfo {
		DefaultMutableTreeNode node;

		int index;
		String uuid;
		boolean passed;
		public StepInfo(DefaultMutableTreeNode node, int index) {
			super();
			this.node = node;
			Object userObject = node.getUserObject();
			if(node.isRoot() && userObject instanceof String){
				index = 0;
			}
			else try{
				uuid = ((HavingUuid)userObject).getUuid();
			}
			catch (Exception e) {
				System.err.println("error for "+userObject+ ":"+e.getMessage());
				this.index = index;
			}
			
			passed = false;
		}		
	}
	
	private static char SEPARATOR = ';';
	private static char BACK_CHAR = 'r';
	
	public static String getSnapshot(JTree tree){
		StringBuilder sb = new StringBuilder();
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		LinkedList<StepInfo> stack = new LinkedList<StepInfo>();
		StepInfo root = new StepInfo((DefaultMutableTreeNode)model.getRoot(),0);
		if(tree.isExpanded(new TreePath(root.node.getPath()))){
			stack.addFirst(root);
		}

		StepInfo curLevel;
		boolean isRoot = true;
		while(! stack.isEmpty()){
			curLevel = stack.getFirst();
			if(curLevel.uuid == null) curLevel.uuid = ""+curLevel.index;
			if(curLevel.passed){
				stack.removeFirst();
				if(stack.isEmpty()) break;
				sb.append(BACK_CHAR);
			}
			else {
				if(isRoot) {
					curLevel.uuid = "0";
					isRoot = false;
				}
				sb.append(curLevel.uuid).append(SEPARATOR);
				for(int i = 0; i < curLevel.node.getChildCount(); ++i){
					DefaultMutableTreeNode child = (DefaultMutableTreeNode)curLevel.node.getChildAt(i);
					if(tree.isExpanded(new TreePath(child.getPath()))){
						stack.addFirst(new StepInfo(child,i));
					}
				}
				curLevel.passed = true;
			}
		}
		return sb.toString();
	}
	
	public static void applaySnapshot(JTree tree,String data) throws IllegalArgumentException {
		if(tree == null || data == null) return;
		
		((DefaultTreeModel)tree.getModel()).reload();
		//парсинг даты
		//новый формат: 0;3829873523;r233239840932;209384098324;4345435345;35345345;rrrr345435435345;r
		//старый формат: 0;3;r2;1;0;0;rrrr0;r

		StringBuilder curOp = new StringBuilder();
		char c;
		DefaultMutableTreeNode curNode = (DefaultMutableTreeNode)((DefaultTreeModel)tree.getModel()).getRoot();
		DefaultMutableTreeNode node;
		//		boolean waiting
		for(int i=2; i < data.length(); ++i){
			c = data.charAt(i);
			if(c == SEPARATOR) {
				String op = curOp.toString();
				node = applayNextLevel(op,tree,curNode);
				if(node == null) {
					node = applayNextLevelOldFormat(op,tree,curNode);
				}
				if(node != null) {
					curNode = node;
					curOp = new StringBuilder();
				}
				else {
					System.err.println("invalid op:" + op);
					return;
				}
			}
			else if(c == BACK_CHAR){
				curNode = (DefaultMutableTreeNode) curNode.getParent();
				if(curNode == null) {
					System.err.println("curNode is null for data:" + data);
					return;
				}
			}
			else {
				curOp.append(c);
			}
		}
	}
	
	
	

	private static DefaultMutableTreeNode applayNextLevel(String curOp, JTree tree,DefaultMutableTreeNode curNode) {
		try {
			String uuid = curOp;
			for (Enumeration<?> e = curNode.children(); e.hasMoreElements();){
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)e.nextElement();
				Object ob = child.getUserObject();
				if (ob instanceof HavingUuid) {
					HavingUuid hu = (HavingUuid) ob;
					if(hu.getUuid().equals(uuid)){
						tree.expandPath(new TreePath(child.getPath()));
						return child;
					}
				}
			}
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private static DefaultMutableTreeNode applayNextLevelOldFormat(String curOp, JTree tree,DefaultMutableTreeNode curNode) {
		try {
			int i = Integer.parseInt(curOp);
			if(i < 0 || i >= curNode.getChildCount()) {
				return null;
			}
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)curNode.getChildAt(i);
			tree.expandPath(new TreePath(child.getPath()));
			return child;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
