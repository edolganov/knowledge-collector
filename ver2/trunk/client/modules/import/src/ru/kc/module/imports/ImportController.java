package ru.kc.module.imports;

import ru.kc.common.controller.Controller;
import ru.kc.common.imports.event.ImportOldClientDataRequest;
import ru.kc.module.imports.command.ImportOldData;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.platform.ui.tabbedform.MainForm;

@Mapping(MainForm.class)
public class ImportController extends Controller<MainForm>{

	@Override
	protected void init() {
		
	}
	
	@EventListener
	public void oldClientImport(ImportOldClientDataRequest request){
		invokeSafe(new ImportOldData(request.importRoot));
	}

}
