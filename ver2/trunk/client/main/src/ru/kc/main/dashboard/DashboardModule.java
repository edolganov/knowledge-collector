package ru.kc.main.dashboard;

import ru.kc.main.dashboard.ui.Dashboard;
import ru.kc.platform.module.Module;

public class DashboardModule extends Module<Dashboard>{

	@Override
	protected Dashboard createUI() {
		return new Dashboard();
	}

}
