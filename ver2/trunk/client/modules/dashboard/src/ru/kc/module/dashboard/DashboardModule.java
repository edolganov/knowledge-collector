package ru.kc.module.dashboard;

import ru.kc.module.dashboard.ui.Dashboard;
import ru.kc.platform.annotations.GlobalMapping;
import ru.kc.platform.module.Module;

@GlobalMapping("dashboard")
public class DashboardModule extends Module<Dashboard>{

	@Override
	protected Dashboard createUI() {
		return new Dashboard();
	}

}
