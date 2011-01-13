package ru.kc.util.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Лист, располагающий свои элементы в виде дерева, по принципу:
 * когда заполняется уровень - создаем следующий.
 *
 */
public class LimitedLevelTreeList<T> implements Iterable<T>{
	
	public static class TreeNode<T> {
		private T ob;
		private List<TreeNode<T>> children;
		
		public TreeNode(T ob, int index) {
			super();
			this.ob = ob;
		}

		public T getOb() {
			return ob;
		}
		
		public List<TreeNode<T>> getChildren(){
			return children == null? new ArrayList<TreeNode<T>>() : new ArrayList<TreeNode<T>>(children);
		}
	}
	
	private int maxChildInLevel;
	private ArrayList<TreeNode<T>> allNodes = new ArrayList<TreeNode<T>>();
	
	public LimitedLevelTreeList() {
		this(10);
	}
	
	public LimitedLevelTreeList(int childInLevel) {
		this.maxChildInLevel = childInLevel;
	}
	
	public boolean add(T e) {
		if(allNodes.size() > 0){
			findAndAdd(e,false);
		} else {
			addChild(null, e);
		}
		return true;
	};
	
	public T get(int index) {
		return allNodes.get(index).getOb();
	}
	
	
	public TreeNode<T> getParentCandidat() {
		if(allNodes.size() > 0){
			return findAndAdd(null,true);
		} else {
			return null;
		}
		
	}
	
	public void setRoot(T root) {
		if(size() > 0) throw new IllegalStateException("root is already set");
		add(root);
	}
	
	public T getRoot() {
		if(allNodes.size() == 0) return null;
		else return allNodes.get(0).getOb();
	}

	public int size() {
		return allNodes.size();
	}
	
	public int getChildCount(int index){
		TreeNode<T> node = allNodes.get(index);
		if(node == null) return -1;
		else return getChildCount(node);
	}

	public boolean isFullLevel(int index){
		return getChildCount(index) >= maxChildInLevel;
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
	
	

	private TreeNode<T> findAndAdd(T ob, boolean onlyFind) {
		LinkedList<TreeNode<T>> levels = new LinkedList<TreeNode<T>>();
		levels.addLast(allNodes.get(0));
		while(true){
			TreeNode<T> candidat = levels.removeFirst();
			if(canAddChild(candidat)){
				if(!onlyFind){ 
					addChild(candidat,ob);
				}
				return candidat;
			} else {
				for (TreeNode<T> child : candidat.children) {
					levels.addLast(child);
				}
			}
		}
	}
	
	private boolean canAddChild(TreeNode<T> parent) {
		boolean out = parent.children == null || parent.children.size() < maxChildInLevel;
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

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			
			Iterator<TreeNode<T>> it = allNodes.iterator();

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public T next() {
				return it.next().getOb();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public int getMaxChildInLevel() {
		return maxChildInLevel;
	}



	









}
