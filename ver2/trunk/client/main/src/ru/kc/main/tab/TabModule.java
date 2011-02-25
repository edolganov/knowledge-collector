package ru.kc.main.tab;

import java.awt.Component;

import ru.kc.main.tab.ui.TabPanel;
import ru.kc.platform.module.Module;
import ru.kc.platform.ui.tabs.TabbedWrapper;

public class TabModule extends Module<TabPanel>{

	@Override
	protected TabPanel createUI() {
		return new TabPanel();
	}
	
	public void setComponent(Component component){
		getController(TabController.class).setComponent(component);
	}
	
	public Component getComponent(){
		return getController(TabController.class).getComponent();
	}

	public void setTabWrapper(TabbedWrapper tabsWrapper) {
		getController(TabController.class).setTabWrapper(tabsWrapper);
	}

}
