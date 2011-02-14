package ru.kc.module.dashboard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import ru.kc.common.controller.Controller;
import ru.kc.module.dashboard.ui.Dashboard;
import ru.kc.platform.action.MethodAction;
import ru.kc.platform.annotations.Mapping;

@Mapping(Dashboard.class)
public class DashboardController extends Controller<Dashboard>{

	@Override
	public void init() {
		JPanel centerPanel = ui.centerPanel;
		centerPanel.setLayout(new BorderLayout());
		Component tree = instanceByMapping("nodes-tree");
		centerPanel.add(tree,BorderLayout.CENTER);
		
		JPanel rightPanel = ui.rightPanel;
		rightPanel.setLayout(new BorderLayout());
		Component props = instanceByMapping("props");
		rightPanel.add(props,BorderLayout.CENTER);
		rightPanel.setMinimumSize(new Dimension(0,0));

		refreshToolbar();
	}

	private void refreshToolbar() {
		ui.toolbar.removeAll();
		List<MethodAction> acitons = getSubActionsRecursive();
		for (MethodAction action : acitons) {
			ui.toolbar.add(action.createButton(true));
		}
	}

}
