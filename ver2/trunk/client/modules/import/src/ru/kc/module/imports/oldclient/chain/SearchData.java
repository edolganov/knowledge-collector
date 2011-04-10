package ru.kc.module.imports.oldclient.chain;

import java.io.File;

import ru.kc.util.workflow.ChainObject;

public class SearchData extends AbstractChainObject {

	@Override
	public ChainObject getInvokeAndGetNext() throws Exception {
		textModel.addText("Scanning "+dataDir.getAbsolutePath());
		File rootData = findDataFile();
		if(rootData == null){
			throw new IllegalStateException("can't find data dir");
		}
		
		return new ProcessData(rootData.getParentFile());
	}

	private File findDataFile() {
		File out = getDataFile(dataDir);
		if(out == null){
			File knowDir = findFile("know", dataDir, false);
			if(knowDir != null){
				out = getDataFile(knowDir);
			}
		}
		return out;
	}

}
