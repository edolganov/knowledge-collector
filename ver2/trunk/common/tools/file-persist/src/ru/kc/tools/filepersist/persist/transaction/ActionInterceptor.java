package ru.kc.tools.filepersist.persist.transaction;




public interface ActionInterceptor {
	
	Class<?>[] types();
	
	void afterInvoke (Object actionObject, Object result);
	
	

}
