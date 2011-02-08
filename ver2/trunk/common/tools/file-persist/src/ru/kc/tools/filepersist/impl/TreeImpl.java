package ru.kc.tools.filepersist.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.kc.model.Dir;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.TreeListener;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class TreeImpl implements Tree {
	
	private Context c;
	private FileSystemImpl fs;
	private CopyOnWriteArrayList<TreeListener> listeners = new CopyOnWriteArrayList<TreeListener>();
	
	
	public void init(Context c) throws Exception{
		this.c = c;
		this.fs = c.fs;
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
	public void addListener(TreeListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public Node getRoot() throws Exception{
		return fs.getRoot();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Node> getChildren(Node node)throws Exception {
		if(node == null) throw new NullPointerException("node");
		return (List)fs.getChildren(convert(node));
	}
	
	@Override
	public void add(Node parent, Node node) throws Exception {
		if(parent == null) throw new NullPointerException("parent");
		if(node == null) throw new NullPointerException("node");
		fs.create(convert(parent), convert(node));
		fireAddedEvent(parent,node);
	}
	
	
	

	private void fireAddedEvent(Node parent, Node node) {
		for(TreeListener l : listeners)l.onAdded(parent, node);
	}

	private NodeBean convert(Node node) {
		if(node != null){
			if(node instanceof NodeBean) return (NodeBean) node;
			//else
			throw new IllegalArgumentException("unknow node type: "+node.getClass());
		} else {
			throw new IllegalArgumentException("node is null");
		}

	}



}
