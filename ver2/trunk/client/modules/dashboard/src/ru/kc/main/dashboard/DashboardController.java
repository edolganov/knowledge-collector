package ru.kc.main.dashboard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;

import ru.kc.main.common.Controller;
import ru.kc.main.dashboard.ui.Dashboard;
import ru.kc.platform.action.MethodAction;
import ru.kc.platform.annotations.Mapping;

@Mapping(Dashboard.class)
public class DashboardController extends Controller<Dashboard>{

	@Override
	public void init() {
		JPanel centerPanel = ui.centerPanel;
		centerPanel.setLayout(new BorderLayout());
		Component tree = instanceForName("ru.kc.main.tree.TreeModule");
		centerPanel.add(tree,BorderLayout.CENTER);

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
