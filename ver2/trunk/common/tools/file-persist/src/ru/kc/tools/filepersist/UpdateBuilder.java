package ru.kc.tools.filepersist;

import java.util.List;

import ru.kc.tools.filepersist.update.UpdateRequest;

public interface UpdateBuilder {
	
	UpdateBuilder add(UpdateRequest request);
	
	List<UpdateRequest> list();

}
