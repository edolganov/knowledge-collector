package ru.kc.module.imports.oldclient.chain;

import java.io.File;

import ru.kc.module.imports.oldclient.model.ImportOldDataTextModel;
import ru.kc.util.annotation.Inject;
import ru.kc.util.workflow.ChainObject;

public class SearchData implements ChainObject {
	
	@Inject File dataDir;
	@Inject ImportOldDataTextModel textModel;

	@Override
	public ChainObject getInvokeAndGetNext() throws Exception {
		textModel.addText("Scanning "+dataDir.getAbsolutePath());
		
		return null;
	}

}
