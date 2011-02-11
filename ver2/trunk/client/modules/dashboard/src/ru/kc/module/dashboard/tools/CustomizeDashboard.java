package ru.kc.module.dashboard.tools;

import ru.kc.main.common.Controller;
import ru.kc.module.dashboard.ui.Dashboard;
import ru.kc.platform.annotations.Mapping;

@Mapping(Dashboard.class)
public class CustomizeDashboard extends Controller<Dashboard>{
	
	@Override
	public void init() {
		ui.jSplitPane1.setDividerLocation(200);
		
		ui.jSplitPane2.setDividerLocation(600);
	}

}
