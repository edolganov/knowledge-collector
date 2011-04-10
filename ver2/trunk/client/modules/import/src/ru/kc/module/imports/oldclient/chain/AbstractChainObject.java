package ru.kc.module.imports.oldclient.chain;

import java.io.File;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.model.Node;
import ru.kc.module.imports.oldclient.DataLoader;
import ru.kc.module.imports.oldclient.model.ImportOldDataTextModel;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.util.annotation.Inject;
import ru.kc.util.workflow.ChainObject;

public abstract class AbstractChainObject implements ChainObject {
	
	protected Log log = LogFactory.getLog(getClass());
	@Inject 
	protected File dataDir;
	@Inject 
	protected ImportOldDataTextModel textModel;
	@Inject
	protected Map<String, Object> invokeContext;
	@Inject
	protected DataLoader dataLoader;
	@Inject
	protected Node beginRootNode;
	@Inject
	protected PersistService persistService;
	

	
	
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
