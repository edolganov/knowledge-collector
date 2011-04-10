package ru.kc.module.imports.oldclient.chain;

import java.io.File;
import java.util.Map;

import ru.kc.module.imports.oldclient.DataLoader;
import ru.kc.module.imports.oldclient.model.ImportOldDataTextModel;
import ru.kc.util.annotation.Inject;
import ru.kc.util.workflow.ChainObject;

public abstract class AbstractChainObject implements ChainObject {
	
	@Inject 
	protected File dataDir;
	@Inject 
	protected ImportOldDataTextModel textModel;
	@Inject
	protected Map<String, Object> invokeContext;
	@Inject
	protected DataLoader dataLoader;
	
	
	protected File getDataFile(File parent){
		File[] listFiles = parent.listFiles();
		if(listFiles != null){
			for(File file : listFiles){
				if(file.isFile()){
					if("data.xml".equals(file.getName())){
						return file;
					}
				}
			}
		}
		return findFile("data.xml", parent, true);
	}
	
	protected File findFile(String name, File parent, boolean isFile){
		File[] listFiles = parent.listFiles();
		if(listFiles != null){
			for(File file : listFiles){
				if(file.isFile()){
					if(!isFile) continue;
				} else {
					if(isFile) continue;
				}
				if(name.equals(file.getName())){
					return file;
				}
			}
		}
		return null;
	}
	
	

}
