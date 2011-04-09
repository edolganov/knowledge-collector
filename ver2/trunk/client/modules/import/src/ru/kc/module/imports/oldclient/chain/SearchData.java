package ru.kc.module.imports.oldclient.chain;

import java.io.File;

import ru.kc.util.workflow.ChainObject;

public class SearchData implements ChainObject {
	
	File dataDir;

	@Override
	public ChainObject getInvokeAndGetNext() throws Exception {
		
		return null;
	}

}
