package ru.kc.main.command.basic;

import ru.kc.common.command.Command;
import ru.kc.common.command.RollbackableCommand;
import ru.kc.model.Node;

public class UpdateNode extends RollbackableCommand<Void>{
	
	Node node;
	

	@Override
	protected Void invoke() throws Exception {
		
		return null;
	}

	@Override
	public void rollback() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
