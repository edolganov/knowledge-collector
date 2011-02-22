package ru.kc.tools.filepersist.persist.transaction;

public interface AtomicActionListener<T extends AtomicAction<?>> {
	
	void onInvoke(T action) throws Throwable;
	
	void onCommit(T action) throws Throwable;
	
	void onRollback(T action) throws Throwable;

}
