package ru.kc.util.workflow;

public interface ChainListener {
	
	boolean continueAfter(ChainObject ob);
	
	void onCanceled(ChainObject ob);
	
	void onFinish();
	
	void onException(ChainObject ob, Exception e);

}
