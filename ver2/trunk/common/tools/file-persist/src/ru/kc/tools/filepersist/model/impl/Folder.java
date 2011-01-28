package ru.kc.tools.filepersist.model.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class Folder implements Iterable<Container> {
	
	public final File file;
	ArrayList<Container> containers = new ArrayList<Container>();
	int containersMaxCount;
	
	public Folder(File file, int containersMaxCount){
		this.file = file;
		this.containersMaxCount = containersMaxCount;
	}
	
	public void add(Container container) {
		checkFull();
		checkExist(container);
		containers.add(container);
	};
	
	private void checkFull() {
		if(isFull()) throw new IllegalStateException(this+" is full");
	}

	private void checkExist(Container container) {
		for(Container child : containers)
			if(child.getFile().equals(container.getFile()))
				throw new IllegalStateException(child+" already exists in "+this);
	}

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
		return "Folder [file=" + file + 
				",\n maxSize="+ containersMaxCount + ", curSize="+containers.size() 
				+",\n containers:"+containers+ "]";
	}

	@Override
	public Iterator<Container> iterator() {
		return containers.iterator();
	}






	
	

}
