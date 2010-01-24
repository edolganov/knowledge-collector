package ru.dolganov.tool.knowledge.collector.dao.fs;

import java.util.List;

import ru.chapaj.util.lang.ClassUtil;
import ru.dolganov.tool.knowledge.collector.dao.exception.NodeExistException;

import model.knowledge.NodeMeta;
import model.knowledge.Root;
import model.knowledge.role.Parent;

public class DAOCheck {

	FSDAOImpl dao;
	
	public DAOCheck(FSDAOImpl fsdaoImpl) {
		this.dao = fsdaoImpl;
	}

	public void checkNoExist(Parent parent, NodeMeta child) {
		Root root = null;
		if (parent instanceof Root) {
			root = (Root) parent;
		}
		else if (parent instanceof NodeMeta) {
			NodeMeta parentNode = (NodeMeta) parent;
			root = dao.getRoot(parentNode,true);
		}
		
		if(root == null) return;
		
		String name = child.getName();
		Class<? extends NodeMeta> childClass = child.getClass();
		checkNoExist(root, name, childClass);
	}
	
	public void checkNoExist(Root root, String name, Class<? extends NodeMeta> childClass) {
		List<NodeMeta> nodes = root.getNodes();
		for (NodeMeta n : nodes) {
			if(n.getName().equals(name) && ClassUtil.isValid(childClass, n.getClass())){
				throw new NodeExistException(name);
			}
		}
		
		
	}

}
