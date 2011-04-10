package ru.kc.module.imports.oldclient.chain;

import java.io.File;

import ru.kc.util.workflow.ChainObject;

public class ProcessData extends AbstractChainObject {
	
	File parentDir;
	
	public ProcessData(File parentDir) {
		super();
		this.parentDir = parentDir;
	}

	@Override
	public ChainObject getInvokeAndGetNext() throws Exception {
		
		return null;
	}
	
	

}
