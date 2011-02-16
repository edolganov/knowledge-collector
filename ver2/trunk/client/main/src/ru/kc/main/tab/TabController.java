package ru.kc.main.tab;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import ru.kc.common.controller.Controller;
import ru.kc.main.tab.ui.TabPanel;
import ru.kc.platform.action.MethodAction;
import ru.kc.platform.annotations.Mapping;

@Mapping(TabPanel.class)
public class TabController extends Controller<TabPanel>{

	@Override
	protected void init() {
		ui.root.setLayout(new BorderLayout());
		ui.toolbar.removeAll();
	}

	public void setComponent(Component component) {
		ui.root.add(component);
		refreshToolbar();
	}
	
	private void refreshToolbar() {
		ui.toolbar.removeAll();
		List<MethodAction> acitons = getSubActionsRecursive();
		for (MethodAction action : acitons) {
			ui.toolbar.add(action.createButton(true));
		}
	}
	
	

}
