package ru.kc.main.dashboard.tools;

import ru.kc.main.dashboard.ui.Dashboard;
import ru.kc.platform.controller.AbstractController;
import ru.kc.platform.controller.annotations.Mapping;

@Mapping(Dashboard.class)
public class CustomizeDashboard extends AbstractController<Dashboard>{
	
	@Override
	public void init() {
		ui.jSplitPane1.setDividerLocation(200);
		
		ui.jSplitPane2.setDividerLocation(600);
	}

}
