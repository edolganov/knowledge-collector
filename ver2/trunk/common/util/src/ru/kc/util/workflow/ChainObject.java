package ru.kc.util.workflow;

public interface ChainObject {
	
	ChainObject getInvokeAndGetNext() throws Exception;

}
