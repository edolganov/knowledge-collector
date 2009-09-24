package ru.dolganov.tool.knowledge.collector.actions;

import java.util.Map;

import model.knowledge.NodeMeta;
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
		if(parent == null) return;
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

}
