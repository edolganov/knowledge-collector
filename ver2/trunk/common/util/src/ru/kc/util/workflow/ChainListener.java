package ru.kc.util.workflow;

public interface ChainListener {
	
	void onDone(ChainObject ob);
	
	void onFinish();
	
	void onException(ChainObject ob, Exception e);

}
