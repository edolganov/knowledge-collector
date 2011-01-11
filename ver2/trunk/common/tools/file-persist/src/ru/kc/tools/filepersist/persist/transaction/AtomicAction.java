package ru.kc.tools.filepersist.persist.transaction;

public abstract class AtomicAction<O> {
	
	
	protected Transaction t;
	
	protected abstract O invoke() throws Exception;
	
	protected abstract void rollback() throws Exception;
	
	
	public void setTransaction(Transaction transaction) {
		this.t = transaction;
	}
	@Override
	public String toString() {
		return ""+getClass().getSimpleName();
	}
	
	
	

}
