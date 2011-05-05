package ru.kc.main.tab;

import java.awt.Component;

import ru.kc.common.FocusProvider;
import ru.kc.main.tab.ui.TabPanel;
import ru.kc.platform.domain.annotation.Domain;
import ru.kc.platform.module.Module;

@Domain
public class TabModule extends Module<TabPanel> implements FocusProvider {

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

	@Override
	public void setFocusRequest() {
		getController(TabController.class).setFocusRequest();
	}

}
