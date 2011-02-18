package ru.kc.tools.filepersist.impl;

import java.util.ArrayList;
import java.util.List;

import ru.kc.tools.filepersist.UpdateBuilder;
import ru.kc.tools.filepersist.update.UpdateRequest;

public class UpdateBuilderImpl implements UpdateBuilder {

	ArrayList<UpdateRequest> updates = new ArrayList<UpdateRequest>();
	
	@Override
	public UpdateBuilder add(UpdateRequest request) {
		updates.add(request);
		return this;
	}

	@Override
	public List<UpdateRequest> list() {
		return updates;
	}

}
