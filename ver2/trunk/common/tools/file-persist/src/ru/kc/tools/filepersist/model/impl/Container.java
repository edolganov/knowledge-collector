package ru.kc.tools.filepersist.model.impl;

import java.io.File;
import java.util.ArrayList;

import ru.kc.tools.filepersist.PersistService;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("container")
public class Container {
	
	@XStreamOmitField 
	private File file;
	
	@XStreamOmitField 
	private int maxSize = Integer.MAX_VALUE;
	
	@XStreamOmitField
	private PersistService persistService;
	
	private ArrayList<NodeBean> nodes = new ArrayList<NodeBean>();

	public void init(File file,PersistService service){
		setFile(file);
		setMaxSize(100);
		setPersistService(service);
	}
	
	public void add(NodeBean node){
		if(isFull()) throw new IllegalStateException("container is full");
		nodes.add(node);
	}
	
	public boolean isFull(){
		return nodes.size() >= maxSize;
	}
	
	public int size(){
		return nodes.size();
	}
	
	public NodeBean get(int index){
		return nodes.get(index);
	}
	
	public void replace(int index, NodeBean node) {
		if(nodes.size() > index+1){
			nodes.remove(index);
		} 
		nodes.add(index,node);
	}
	
	public NodeBean getFirst(){
		if(size() > 0){
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
	public PersistService getPersistService() {
		return persistService;
	}

	public void setPersistService(PersistService persistService) {
		this.persistService = persistService;
	}
	
	

	public static Container create(File file,PersistService service){
		Container container = new Container();
		container.init(file, service);
		return container;
	}
	



	

}
