package ru.kc.tools.filepersist.persist.transaction;

import ru.kc.tools.filepersist.persist.FSContext;

public abstract class AtomicAction<O> {
	
	
	protected Transaction<?> t;
	protected FSContext c;
	
	protected abstract O invoke() throws Throwable;
	
	protected abstract void rollback() throws Throwable;
	
	
	public void init(Transaction<?> transaction,FSContext c) {
		this.c = c;
		this.t = transaction;
	}
	@Override
	public String toString() {
		return ""+getClass().getSimpleName();
	}
	
	
	

}
