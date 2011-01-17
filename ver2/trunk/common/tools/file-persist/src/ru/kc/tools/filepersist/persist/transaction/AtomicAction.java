package ru.kc.tools.filepersist.persist.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.filepersist.persist.FSContext;

public abstract class AtomicAction<O> {
	
	
	protected Transaction<?> t;
	protected FSContext c;
	protected Log log;
	
	protected abstract O invoke() throws Throwable;
	
	protected abstract void rollback() throws Throwable;
	
	
	public void init(Transaction<?> transaction,FSContext c) {
		this.c = c;
		this.t = transaction;
		this.log = LogFactory.getLog(getClass());
	}
	@Override
	public String toString() {
		return ""+getClass().getSimpleName();
	}
	
	
	

}
