package ru.kc.util.collection;

import java.util.ArrayList;
import java.util.Collection;

public class LimitedList<E> extends ArrayList<E> {
	
	private int maxSize = Integer.MAX_VALUE;
	
	public boolean add(E e) {
		if(isFull()) throw new IllegalStateException("list is full");
		return super.add(e);
	};
	
	public void add(int index, E element) {
		if(isFull()) throw new IllegalStateException("list is full");
		super.add(index,element);
	};
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		//TODO
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		//TODO
		throw new UnsupportedOperationException();
	}
	
	public E getLast() {
		return get(size()-1);
	}
	
	
	public boolean isFull(){
		return size() >= maxSize;
	}
	
	public int getMaxSize() {
		return maxSize;
	}
	
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
	
	public static <M> LimitedList<M> create(M first){
		LimitedList<M> list = new LimitedList<M>();
		list.setMaxSize(100);
		list.add(first);
		return list;
	}



}
