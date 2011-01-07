package ru.dolganov.tool.knowledge.collector.dao.fs;

import java.util.List;

import ru.chapaj.util.lang.ClassUtil;
import ru.dolganov.tool.knowledge.collector.dao.exception.NodeExistException;

import model.knowledge.Node;
import model.knowledge.Root;
import model.knowledge.RootElement;
import model.knowledge.role.Parent;

public class DAOCheck {

	FSDAOImpl dao;
	
	public DAOCheck(FSDAOImpl fsdaoImpl) {
		this.dao = fsdaoImpl;
	}

	public void checkNoExist(Parent parent, Node child) {
		Root root = null;
		if (parent instanceof Root) {
			root = (Root) parent;
		}
		else if (parent instanceof Node) {
			Node parentNode = (Node) parent;
			root = dao.getRoot(parentNode,true);
		}
		
		if(root == null) return;
		
		String name = child.getName();
		Class<? extends Node> childClass = child.getClass();
		checkNoExist(root, name, childClass);
	}
	
	public void checkNoExist(Root root, String name, Class<? extends Node> childClass) {
		List<RootElement> nodes = root.getNodes();
		for (RootElement n : nodes) {
			if(n instanceof Node){
				if(((Node)n).getName().equals(name) && ClassUtil.isValid(childClass, n.getClass())){
					throw new NodeExistException(name);
				}
			}
		}
		
		
	}

}
