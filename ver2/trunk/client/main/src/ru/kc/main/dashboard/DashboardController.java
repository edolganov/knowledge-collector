package ru.kc.main.dashboard;

import ru.kc.main.dashboard.ui.Dashboard;
import ru.kc.platform.controller.Controller;
import ru.kc.platform.controller.annotations.Mapping;

@Mapping(Dashboard.class)
public class DashboardController extends Controller<Dashboard>{

	@Override
	public void init(Dashboard ui) {
		System.out.println("work!!!!!!!!!!");
	}

}
