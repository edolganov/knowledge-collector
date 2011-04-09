package ru.kc.common.imports.event;

import ru.kc.model.Node;
import ru.kc.platform.event.Request;

public class ImportOldClientDataRequest extends Request<Void>{
	
	public final Node importRoot;

	public ImportOldClientDataRequest(Node importRoot) {
		super();
		this.importRoot = importRoot;
	}
	
	

}
