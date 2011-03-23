package ru.kc.module.snapshots.event;

import ru.kc.platform.domain.annotation.DomainSpecific;
import ru.kc.platform.event.Request;

@DomainSpecific
public class CreateSnapshotDirRequest extends Request<Integer>{
	
	public final String name;

	public CreateSnapshotDirRequest(String name) {
		super();
		this.name = name;
	}
	
	
	

}
