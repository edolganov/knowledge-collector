package ru.kc.tools.filepersist.model.impl;

import java.io.File;
import java.util.ArrayList;

import ru.kc.tools.filepersist.Tree;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("container")
public class Container {

	@XStreamOmitField
	private File file;
	
	@XStreamOmitField
	private int maxSize = Integer.MAX_VALUE;
	
	@XStreamOmitField
	private Tree persistService;

	private long revision;
	private ArrayList<NodeBean> nodes = new ArrayList<NodeBean>();

	
	

	public void init(File file, Tree service, int maxSize) {
		setFile(file);
		setMaxSize(maxSize);
		setPersistService(service);
	}

	public void add(NodeBean node) {
		if (isFull())
			throw new IllegalStateException("container is full");
		nodes.add(node);
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

	@Override
	public String toString() {
		return "Container [file=" + file + ", maxSize=" + maxSize + ", nodes="
				+ nodes + "]";
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

	public Tree getPersistService() {
		return persistService;
	}

	public void setPersistService(Tree persistService) {
		this.persistService = persistService;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}

	public static Container create(File file, Tree service,int maxSize) {
		Container container = new Container();
		container.init(file, service,maxSize);
		return container;
	}



}
