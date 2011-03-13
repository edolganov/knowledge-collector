package ru.kc.module.tree.tools;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.command.CreateDirRequest;
import ru.kc.common.node.command.CreateFileLinkRequest;
import ru.kc.common.node.command.CreateLinkRequest;
import ru.kc.common.node.command.CreateTextRequest;
import ru.kc.model.Node;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.ExportAction;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class ExportActionsController extends Controller<Tree> {
	
	
	TreeFacade treeFacade;
	
	@Override
	protected void init() {
		
		treeFacade = new TreeFacade(ui.tree);
		
	}

	
	@ExportAction(description="create dir", icon="/ru/kc/common/img/createDir.png")
	public void createDirRequest(){
		Node parent = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new CreateDirRequest(parent));
	}
	
	@ExportAction(description="create link", icon="/ru/kc/common/img/createLink.png")
	public void createLinkRequest(){
		Node parent = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new CreateLinkRequest(parent));
	}
	
	@ExportAction(description="create text", icon="/ru/kc/common/img/createText.png")
	public void createTextRequest(){
		Node parent = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new CreateTextRequest(parent));
	}
	
	@ExportAction(description="create file link", icon="/ru/kc/common/img/createFileLink.png")
	public void createFileLinkRequest(){
		Node parent = treeFacade.getCurrentObject(Node.class);
		invokeSafe(new CreateFileLinkRequest(parent));
	}
}
