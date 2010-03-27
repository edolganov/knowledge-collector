package ru.dolganov.tool.knowledge.collector.command.ops;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import model.knowledge.Dir;
import model.knowledge.Node;
import model.knowledge.RootElement;
import model.knowledge.TextData;

public class TreeOps {

	
	
	public static RootElement[] getMoveElements(DefaultMutableTreeNode tagretNode, DefaultMutableTreeNode draggedNode){
		if(tagretNode == null || draggedNode == null) return null;
		
		if(draggedNode.isRoot()) return null;
		if(tagretNode.equals(draggedNode)) return null;
		Object draggedOb = draggedNode.getUserObject();
		if(draggedOb == null || !(draggedOb instanceof Node)) return null;
		
		//перемещаем только в папку или ноту
		Object targetOb = tagretNode.getUserObject();
		if(!(targetOb instanceof Dir) && !(targetOb instanceof TextData)){
			tagretNode = (DefaultMutableTreeNode) tagretNode.getParent();
			if(tagretNode == null) {
				return null;
			}
			else targetOb = tagretNode.getUserObject();
		}
		if(!(targetOb instanceof Dir) && !(targetOb instanceof TextData)) return null;
		if(draggedNode.getParent().equals(tagretNode)) return null;
		
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
		if(! valid) return null;
		else {
			return new RootElement[]{(RootElement)targetOb, (RootElement)draggedOb};
		}
	}
}
