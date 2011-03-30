package ru.kc.module.snapshots.command;

import ru.kc.common.command.Command;
import ru.kc.common.tree.TreeService;
import ru.kc.common.tree.event.GetTreeServiceRequest;
import ru.kc.model.Node;

public class GetOwner extends Command<Node> {

	@Override
	protected Node invoke() throws Exception {
		
		TreeService service = invokeSafe(new GetTreeServiceRequest()).result;
		if(service != null){
			Node node = service.getRoot().getUserObject(Node.class);
			return node;
		}
		
		return null;
	}

}
