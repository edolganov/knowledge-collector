package ru.kc.util.collection;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Лист, располагающий свои элементы в виде дерева, по принципу:
 * когда заполняется уровень - создаем следующий.
 *
 */
public class TreeList<T> extends AbstractList<T>{
	
	public static class TreeNode<T> {
		private T ob;
		private int index;
		private List<TreeNode<T>> children;
		
		public TreeNode(T ob, int index) {
			super();
			this.ob = ob;
			this.index = index;
		}

		public T getOb() {
			return ob;
		}

		public int getIndex() {
			return index;
		}
	}
	
	private int childInLevel;
	private ArrayList<TreeNode<T>> allNodes = new ArrayList<TreeNode<T>>();
	
	public TreeList() {
		this(10);
	}
	
	public TreeList(int childInLevel) {
		this.childInLevel = childInLevel;
	}
	
	public boolean add(T e) {
		if(allNodes.size() > 0){
			findAndAdd(e);
		} else {
			addChild(null, e);
		}
		return true;
	};
	
	@Override
	public T get(int index) {
		TreeNode<T> node = allNodes.get(index);
		return node != null? node.ob : null;
	}
	
	public T getRoot() {
		if(size() == 0) return null;
		else return get(0);
	}

	@Override
	public int size() {
		return allNodes.size();
	}
	
	public int getChildCount(int index){
		TreeNode<T> node = allNodes.get(index);
		if(node == null) return -1;
		else return getChildCount(node);
	}

	public boolean isFullLevel(int index){
		return getChildCount(index) >= childInLevel;
	}
	
	public List<TreeNode<T>> getChildren(int index){
		TreeNode<T> parent = allNodes.get(index);
		if(parent != null){
			return parent.children != null? new ArrayList<TreeNode<T>>(parent.children) 
					: new ArrayList<TreeNode<T>>(0);
		} else {
			return null;
		}
	}
	
	

	private void findAndAdd(T ob) {
		LinkedList<TreeNode<T>> levels = new LinkedList<TreeNode<T>>();
		levels.addLast(allNodes.get(0));
		while(true){
			TreeNode<T> candidat = levels.removeFirst();
			if(canAddChild(candidat)){
				addChild(candidat,ob);
				break;
			} else {
				for (TreeNode<T> child : candidat.children) {
					levels.addLast(child);
				}
			}
		}
	}
	
	private boolean canAddChild(TreeNode<T> parent) {
		boolean out = parent.children == null || parent.children.size() < childInLevel;
		return out;
	}

	private void addChild(TreeNode<T> parent, T ob) {
		int index = allNodes.size();
		TreeNode<T> child = new TreeNode<T>(ob,index);
		allNodes.add(index,child);
		
		if(parent != null){
			if(parent.children == null){
				parent.children = new ArrayList<TreeNode<T>>();
			} 
			parent.children.add(child);
		}
	}
	
	private int getChildCount(TreeNode<T> node) {
		if(node.children == null) return 0;
		else return node.children.size();
	}







}
