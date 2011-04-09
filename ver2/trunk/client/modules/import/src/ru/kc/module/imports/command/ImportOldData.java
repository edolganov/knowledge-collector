package ru.kc.module.imports.command;

import java.io.File;

import javax.swing.JFileChooser;

import ru.kc.common.command.Command;
import ru.kc.model.Node;
import ru.kc.module.imports.tools.ImportOldDataModule;

public class ImportOldData extends Command<Void>{
	
	Node importRoot;
	
	

	public ImportOldData(Node importRoot) {
		super();
		this.importRoot = importRoot;
	}

	@Override
	protected Void invoke() throws Exception {
		File dataDir = getDataDir();
		if(dataDir == null) return null;
		
		ImportOldDataModule module = new ImportOldDataModule();
		module.createDialog(rootUI, true);
		module.setImportData(importRoot, dataDir);
		module.show();
		
		return null;
	}

	private File getDataDir() {
		
		JFileChooser fc = new JFileChooser(new File("./.."));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(rootUI);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file;
		} else {
			return null;
		}
	}

}
