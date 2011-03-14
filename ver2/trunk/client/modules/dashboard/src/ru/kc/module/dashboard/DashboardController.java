package ru.kc.module.dashboard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;

import ru.kc.common.controller.Controller;
import ru.kc.module.dashboard.ui.Dashboard;
import ru.kc.platform.annotations.Mapping;

@Mapping(Dashboard.class)
public class DashboardController extends Controller<Dashboard>{

	@Override
	public void init() {
		
		JPanel rightPanel = ui.rightPanel;
		rightPanel.setLayout(new BorderLayout());
		Component props = instanceByMapping("props");
		rightPanel.add(props,BorderLayout.CENTER);
		rightPanel.setMinimumSize(new Dimension(0,0));
		
		JPanel centerPanel = ui.centerPanel;
		centerPanel.setLayout(new BorderLayout());
		Component tree = instanceByMapping("nodes-tree");
		centerPanel.add(tree,BorderLayout.CENTER);
		
		ui.leftPanel.setMinimumSize(new Dimension(0, 0));
		JPanel searchPanel = ui.search;
		searchPanel.setLayout(new BorderLayout());
		Component search = instanceByMapping("search");
		searchPanel.add(search,BorderLayout.CENTER);
		
		setDividersSize();
	}
	
	private void setDividersSize() {
		ui.jSplitPane2.setDividerLocation(500);
	}

}
