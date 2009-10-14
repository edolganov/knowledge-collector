package ru.dolganov.tool.knowledge.collector.actions;

import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import model.knowledge.Dir;
import model.knowledge.Link;
import model.knowledge.NodeMeta;
import model.knowledge.TextData;
import ru.chapaj.util.Check;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.dolganov.tool.knowledge.collector.App;
import ru.dolganov.tool.knowledge.collector.dao.DAO;

public class Actions {
	
	static ExtendTree tree = App.getDefault().getUI().tree;
	static DAO dao = App.getDefault().getDao();
	
	public static void deleteCurrentTreeNode(){
		NodeMeta node = getCurMeta();
		if(node == null) return;
		dao.delete(node);
	}

	public static void addTreeNode(NodeMeta node) {
		addTreeNode(getCurMeta(), node);
	}
	
	public static void addTreeNode(NodeMeta parent, NodeMeta node) {
		if(parent == null || node == null) return;
		if(node instanceof Link){
			Link l = (Link) node;
			String name = l.getName();
			String url = l.getUrl();
			if(Check.isEmpty(name)){
				l.setName(url);
			}
			else if(Check.isEmpty(url)){
				l.setUrl(name);
			}
		}
		dao.addChild(parent, node);
	}

	public static void updateCurrentTreeNode(Map<String, String> params) {
		NodeMeta node = getCurMeta();
		if(node == null) return;
		dao.update(node,params);
	}
	
	
	private static NodeMeta getCurMeta(){
		Object ob = tree.getCurrentObject();
		if(ob == null) return null;
		
		if (ob instanceof NodeMeta) {
			return (NodeMeta) ob;
		}
		
		return null;
	}

	public static void move(DefaultMutableTreeNode tagretNode,
			DefaultMutableTreeNode draggedNode) {
		if(draggedNode.isRoot()) return;
		if(tagretNode.equals(draggedNode)) return;
		Object draggedOb = draggedNode.getUserObject();
		if(draggedOb == null || !(draggedOb instanceof NodeMeta)) return;
		
		//перемещаем только в папку или ноту
		Object targetOb = tagretNode.getUserObject();
		if(!(targetOb instanceof Dir) && !(targetOb instanceof TextData)){
			tagretNode = (DefaultMutableTreeNode) tagretNode.getParent();
			if(tagretNode == null) {
				return;
			}
			else targetOb = tagretNode.getUserObject();
		}
		if(!(targetOb instanceof Dir) && !(targetOb instanceof TextData)) return;
		if(draggedNode.getParent().equals(tagretNode)) return;
		
		//проверяем что предок не перемещается в потомка
		TreeNode[] targetPath = tagretNode.getPath();
		TreeNode[] candidatPath = draggedNode.getPath();
		boolean valid = false;
		if(candidatPath.length > targetPath.length) valid = true;
		else {
			for (int i = 0; i < candidatPath.length; i++) {
				if(!candidatPath[i].equals(targetPath[i])){
					valid = true;
					break;
				}
			}
		}
		if(valid){
			addTreeNode((NodeMeta)targetOb, (NodeMeta)draggedOb);
		}
		
	}

}
