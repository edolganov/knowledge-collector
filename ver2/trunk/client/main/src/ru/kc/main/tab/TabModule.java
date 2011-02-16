package ru.kc.main.tab;

import java.awt.Component;

import ru.kc.main.tab.ui.TabPanel;
import ru.kc.platform.module.Module;

public class TabModule extends Module<TabPanel>{

	@Override
	protected TabPanel createUI() {
		return new TabPanel();
	}
	
	public void setComponent(Component component){
		getController(TabController.class).setComponent(component);
	}

}
