package ru.kc.module.dashboard;

import ru.kc.common.FocusProvider;
import ru.kc.module.dashboard.ui.Dashboard;
import ru.kc.platform.annotations.GlobalMapping;
import ru.kc.platform.module.Module;

@GlobalMapping("dashboard")
public class DashboardModule extends Module<Dashboard> implements FocusProvider{

	@Override
	protected Dashboard createUI() {
		return new Dashboard();
	}

	@Override
	public void setFocusRequest() {
		getController(DashboardController.class).setFocusRequest();
	}

}
