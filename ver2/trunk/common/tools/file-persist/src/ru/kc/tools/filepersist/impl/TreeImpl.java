package ru.kc.tools.filepersist.impl;

import java.util.Collection;

import ru.kc.model.Dir;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class TreeImpl implements Tree {
	
	private Context c;
	private FileSystemImpl fs;
	
	
	public void init(Context c) throws Exception{
		this.c = c;
		this.fs = new FileSystemImpl();
		fs.init(c.init, this);
		createOrLoadData();
	}

	private void createOrLoadData() throws Exception {
		NodeBean node = fs.getRoot();
		if(node == null){
			Dir dir = c.factory.createDir("root");
			NodeBean root = convert(dir);
			fs.createRoot(root);
		}
	}
	

	
	@Override
	public Node getRoot() throws Exception{
		return fs.getRoot();
	}

	@Override
	public Collection<Node> getChildren(Node node) {
		return fs.getChildren(convert(node));
	}
	
	@Override
	public void add(Node parent, Node node) throws Exception {
		fs.create(convert(parent), convert(parent));
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
