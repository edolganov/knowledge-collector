package ru.kc.module.imports.command;

import ru.kc.common.command.Command;
import ru.kc.model.Node;

public class ImportOldData extends Command<Void>{
	
	Node importRoot;
	
	

	public ImportOldData(Node importRoot) {
		super();
		this.importRoot = importRoot;
	}

	@Override
	protected Void invoke() throws Exception {
		System.out.println("import!!");
		return null;
	}

}
