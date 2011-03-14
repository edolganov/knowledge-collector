package ru.kc.module.tree.tools;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.edit.event.UpdateNodeRequest;
import ru.kc.model.Node;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.component.ComponentUtil;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class CtrlSActionController extends Controller<Tree> {
	
	@SuppressWarnings("serial")
	@Override
	protected void init() {
		final TreeFacade facade = new TreeFacade(ui.tree);
		ComponentUtil.addAction(ui.tree, "control S", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node node = facade.getCurrentObject(Node.class);
				if(node != null){
					fireEvent(new UpdateNodeRequest(node));
				}
			}
		});
	}


}
