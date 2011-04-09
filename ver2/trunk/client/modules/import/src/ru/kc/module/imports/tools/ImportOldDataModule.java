package ru.kc.module.imports.tools;

import java.awt.Frame;
import java.io.File;

import ru.kc.model.Node;
import ru.kc.module.imports.ui.ImportOldDataDialog;
import ru.kc.platform.module.DialogModule;

public class ImportOldDataModule extends DialogModule<ImportOldDataDialog>{
	
	public ImportOldDataModule() {
		super(false);
	}

	@Override
	protected ImportOldDataDialog createUI(Frame parent, boolean modal) {
		return new ImportOldDataDialog(parent, modal);
	}
	
	public void setImportData(Node importRoot, File dataDir){
		getController(ImportOldDataController.class).setImportData(importRoot, dataDir);
	}

}
