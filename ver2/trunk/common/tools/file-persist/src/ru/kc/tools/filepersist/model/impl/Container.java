package ru.kc.tools.filepersist.model.impl;

import java.io.File;
import java.util.ArrayList;

import ru.kc.tools.filepersist.impl.Context;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("container")
public class Container {

	@XStreamOmitField
	private File file;
	
	@XStreamOmitField
	private int maxSize = Integer.MAX_VALUE;
	
	@XStreamOmitField
	private Context context;

	private long revision;
	private ArrayList<NodeBean> nodes = new ArrayList<NodeBean>();

	
	

	public void init(File file,Context context) {
		String path = file.getPath();
		String rootPath = context.init.nodesDir.getPath();
		if(!path.startsWith(rootPath)) throw new IllegalStateException("unknow path: "+path+ " don't start with "+ rootPath);
			
		setFile(file);
		setMaxSize(context.init.params.maxNodesInContainer);
		setContext(context);
	}

	public void add(NodeBean node) {
		checkFull();
		checkExist(node);
		nodes.add(node);
	}
	

	private void checkFull() {
		if (isFull())
			throw new IllegalStateException("container is full");
	}
	
	private void checkExist(NodeBean node) {
		for(NodeBean child : nodes){
			if(child.getId().equals(node.getId()))
				throw new IllegalStateException(child+" already exists in "+this);
		}
	}

	public void remove(NodeBean node) {
		nodes.remove(node);
	}

	public boolean isFull() {
		return nodes.size() >= maxSize;
	}

	public int size() {
		return nodes.size();
	}

	public NodeBean get(int index) {
		return nodes.get(index);
	}

	public void replace(int index, NodeBean node) {
		if (nodes.size() > index + 1) {
			nodes.remove(index);
		}
		nodes.add(index, node);
	}

	public NodeBean getFirst() {
		if (size() > 0) {
			return get(0);
		} else {
			return null;
		}
	}
	
	public NodeBean getRoot() {
		for (NodeBean node : nodes) {
			if(node.getParentId() == null){
				return node;
			}
		}
		
		return null;
	}
	
	public NodeBean find(String childId) {
		for (NodeBean node : nodes) {
			if(node.getId().equals(childId)) return node;
		}
		return null;
	}
	
	
	
	
	public String getFileSimplePath() {
		String path = getFile().getPath();
		String rootPath = context.init.nodesDir.getPath();
		String out = path.substring(rootPath.length()+1);
		return out;
	}

	
	
	@Override
	public String toString() {
		return "Container [file=" + file 
			+ ",\n maxSize=" + maxSize+" ,curSize="+nodes.size() 
			+ ",\n nodes="+ nodes + "]";
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public ArrayList<NodeBean> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<NodeBean> nodes) {
		this.nodes = nodes;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}
	
	
	

	public static Container create(File file, Context c) {
		Container container = new Container();
		container.init(file,c);
		return container;
	}









}
