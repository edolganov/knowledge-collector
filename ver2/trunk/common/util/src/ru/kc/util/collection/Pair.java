package ru.kc.util.collection;

public class Pair<A,B> {
	
	private A first;
	private B second;
	
	public Pair() {
		super();
	}

	public Pair(A first, B second) {
		super();
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return first;
	}

	public void setFirst(A first) {
		this.first = first;
	}

	public B getSecond() {
		return second;
	}

	public void setSecond(B second) {
		this.second = second;
	}
	
	public void copyTo(Pair<A,B> dest){
		dest.setFirst(getFirst());
		dest.setSecond(getSecond());
	}

	@Override
	public String toString() {
		return "Pair [first=" + first + ", second=" + second + "]";
	}
	
	
	
}

