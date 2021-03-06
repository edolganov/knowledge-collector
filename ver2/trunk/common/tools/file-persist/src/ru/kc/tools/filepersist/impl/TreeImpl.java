package ru.kc.tools.filepersist.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.kc.model.Dir;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class TreeImpl implements Tree {
	
	private Context c;
	private FileSystemImpl fs;
	private Listeners listeners;
	
	
	public void init(Context c) throws Exception{
		this.c = c;
		this.fs = c.fs;
		this.listeners = c.listeners;
		createOrLoadData();
	}

	private void createOrLoadData() throws Exception {
		NodeBean node = fs.getRoot();
		if(node == null){
			Dir dir = c.factory.createDir("root",null);
			NodeBean root = convert(dir);
			fs.createRoot(root);
		}
	}
	
	@Override
	public Node getRoot() throws Exception{
		return fs.getRoot();
	}
	
	public Node getParent(Node node) throws Exception {
		if(node == null) throw new NullPointerException("node");
		return fs.getParent(convert(node));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Node> getChildren(Node node)throws Exception {
		if(node == null) throw new NullPointerException("node");
		return (List)fs.getChildren(convert(node));
	}
	
	@Override
	public void add(Node parent, Node child) throws Exception {
		if(parent == null) throw new NullPointerException("parent");
		if(child == null) throw new NullPointerException("child");
		fs.create(convert(parent), convert(child));
		listeners.fireAddedEvent(parent,child);
	}
	
	@Override
	public void deleteRecursive(Node node) throws Exception {
		if(node == null) throw new NullPointerException("node");
		if(node.equals(getRoot())) throw new IllegalArgumentException("can't delete root"); 
		
		Node parent = getParent(node);
		if(parent == null) 
			throw new IllegalStateException("parent is null for "+node);
		List<Node> subChildren = getSubChildren(node);
		fs.deleteRecursive(convert(node));
		listeners.fireDeletedEvent(parent,node, subChildren);
	}

	private List<Node> getSubChildren(Node parent) throws Exception {
		ArrayList<Node> out = new ArrayList<Node>();
		
		LinkedList<Node> parentQueue = new LinkedList<Node>();
		parentQueue.addLast(parent);
		while(parentQueue.size() > 0){
			Node curParent = parentQueue.removeFirst();
			List<Node> children = curParent.getChildren();
			for (Node node : children) {
				out.add(node);
				parentQueue.addLast(node);
			}
		}
		
		return out;
	}
	
	@Override
	public boolean canMove(Node child, Node newParent) throws Exception {
		if(child == null) return false;
		if(newParent == null) return false;
		if(child.equals(getRoot())) return false;
		
		return fs.canMove(convert(child), convert(newParent));
	}
	
	@Override
	public void move(Node child, Node newParent) throws Exception {
		if(child == null) throw new NullPointerException("child");
		if(newParent == null) throw new NullPointerException("newParent");
		if(child.equals(getRoot())) throw new IllegalArgumentException("can't move root");
		
		Node oldParent = getParent(child);
		if(oldParent == null) 
			throw new IllegalStateException("parent is null for "+child);
		
		if(oldParent.equals(newParent))
			return;
		
		fs.move(convert(child), convert(newParent));
		listeners.fireMovedEvent(oldParent, child, newParent);
	}
	
	
	@Override
	public int moveUp(Node node) throws Exception {
		if(node == null) throw new NullPointerException("node");
		if(node.equals(getRoot())) return 0;
		
		Node parent = getParent(node);
		if(parent == null) 
			throw new IllegalStateException("parent is null for "+node);
		
		int newIndex = fs.moveUp(convert(parent), convert(node));
		listeners.fireMovedChildEvent(parent, node, newIndex);
		return newIndex;
	}
	
	@Override
	public int moveDown(Node node) throws Exception {
		if(node == null) throw new NullPointerException("node");
		if(node.equals(getRoot())) return 0;
		
		Node parent = getParent(node);
		if(parent == null) 
			throw new IllegalStateException("parent is null for "+node);
		
		int newIndex = fs.moveDown(convert(parent), convert(node));
		listeners.fireMovedChildEvent(parent, node, newIndex);
		return newIndex;
	}
	
	

	private NodeBean convert(Node node) {
		return c.converter.convert(node);
	}






}
