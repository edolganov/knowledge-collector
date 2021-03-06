package ru.kc.module.tree.tools.hotkeys;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ru.kc.common.controller.Controller;
import ru.kc.common.dashboard.event.OpenHideRightPanel;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.component.ComponentUtil;

@Mapping(Tree.class)
public class Ctrl_H_Controller extends Controller<Tree> {
	
	@SuppressWarnings("serial")
	@Override
	protected void init() {
		ComponentUtil.addAction(ui.tree, "control H", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEvent(new OpenHideRightPanel());
			}
		});
	}


}
