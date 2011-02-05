package ru.kc.main.dashboard;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import ru.kc.main.dashboard.ui.Dashboard;
import ru.kc.main.tree.TreeModule;
import ru.kc.platform.controller.Controller;
import ru.kc.platform.controller.annotations.Mapping;

@Mapping(Dashboard.class)
public class DashboardController extends Controller<Dashboard>{

	@Override
	public void init() {
		JPanel centerPanel = ui.centerPanel;
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(new TreeModule(),BorderLayout.CENTER);
	}

}
