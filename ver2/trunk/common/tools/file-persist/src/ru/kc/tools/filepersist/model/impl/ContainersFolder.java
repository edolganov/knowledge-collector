package ru.kc.tools.filepersist.model.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ContainersFolder implements Iterable<Container> {
	
	public final File file;
	ArrayList<Container> containers = new ArrayList<Container>();
	int containersMaxCount;
	
	public ContainersFolder(File file, int containersMaxCount){
		this.file = file;
		this.containersMaxCount = containersMaxCount;
	}
	
	public void add(Container container) {
		if(isFull()) throw new IllegalStateException(this+" is full");
		containers.add(container);
	};
	
	public Container get(int index) {
		return containers.get(index);
	}
	
	public Container getLast() {
		int size = size();
		if(size>0) return get(size-1);
		else return null;
	}
	
	public boolean isFull(){
		return size() >= containersMaxCount;
	}
	
	public boolean isEmpty() {
		return containers.isEmpty();
	}

	public int size() {
		return containers.size();
	}

	@Override
	public String toString() {
		return "ContainersFolder [file=" + file + ", containersMaxCount="
				+ containersMaxCount + "]";
	}

	@Override
	public Iterator<Container> iterator() {
		return containers.iterator();
	}






	
	

}
