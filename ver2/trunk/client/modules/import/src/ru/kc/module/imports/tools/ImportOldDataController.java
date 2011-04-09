package ru.kc.module.imports.tools;

import java.io.File;

import ru.kc.common.controller.Controller;
import ru.kc.model.Node;
import ru.kc.module.imports.ui.ImportOldDataDialog;
import ru.kc.platform.annotations.Mapping;

@Mapping(ImportOldDataDialog.class)
public class ImportOldDataController extends Controller<ImportOldDataDialog>{

	@Override
	protected void init() {
		
	}
	
	public void setImportData(Node importRoot, File dataDir){
		
	}

}
