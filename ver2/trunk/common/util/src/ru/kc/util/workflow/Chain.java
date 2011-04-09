package ru.kc.util.workflow;

import java.util.List;

import ru.kc.util.reflection.ReflectionUtil;

public class Chain {
	
	private ChainObject curObject;
	private ChainListener chainListener;
	private List<Object> context;
	
	public Chain(ChainObject first, ChainListener chainListener) {
		this(first, null, chainListener);
	}
	
	public Chain(ChainObject first, List<Object> context, ChainListener chainListener) {
		this.chainListener = chainListener;
		this.curObject = first;
		this.context = context;
	}
	
	public void execute(){
		while(curObject != null){
			ChainObject old = null;
			try {
				old = curObject;
				if(context != null){
					ReflectionUtil.injectDataFromContext(curObject, context);
				}
				curObject = curObject.getInvokeAndGetNext();
				
				boolean canContinue = chainListener.continueAfter(old);
				if(!canContinue){
					chainListener.onCanceled(old);
					return;
				}
			}catch (Exception e) {
				chainListener.onException(old, e);
				return;
			}
		}
		chainListener.onFinish();
	}
	
	

}
