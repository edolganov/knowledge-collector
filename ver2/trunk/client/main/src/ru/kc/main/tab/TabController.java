package ru.kc.main.tab;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JToolBar;

import ru.kc.common.controller.Controller;
import ru.kc.common.tab.TabFacade;
import ru.kc.main.tab.ui.TabPanel;
import ru.kc.platform.action.MethodAction;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.ui.tabs.TabbedWrapper;
import ru.kc.util.swing.icon.IconUtil;

@Mapping(TabPanel.class)
public class TabController extends Controller<TabPanel>{

	JToolBar toolbar;
	Component component;
	
	
	@Override
	protected void init() {
		ui.root.setLayout(new BorderLayout());
		toolbar = ui.toolbar;
		toolbar.removeAll();
	}

	public void setComponent(Component component) {
		this.component = component;
		ui.root.add(component);
		refreshToolbar();
	}

	public void setTabWrapper(TabbedWrapper tabsWrapper) {
		initExternalService(tabsWrapper);
	}
	

	private void initExternalService(final TabbedWrapper tabsWrapper) {
		TabFacade facade = new TabFacade() {
			
			@Override
			public void resetMarks() {
				System.out.println("resetMarks");
			}
			
			@Override
			public void markAsModified() {
				System.out.println("markAsModified");
			}
		};
		
	}
	
	

	public Component getComponent() {
		return component;
	}
	
	private void refreshToolbar() {
		toolbar.removeAll();
		
		addSystemButtons();
		addSeparator();
		addCustomButtons();
		

	}
	
	private void addSystemButtons() {
		JButton menu = new JButton(IconUtil.get("/ru/kc/common/img/mainMenu.png"));
		menu.setToolTipText("menu");
		toolbar.add(menu);
	}
	
	private void addSeparator() {
		toolbar.add(new JToolBar.Separator());
	}

	private void addCustomButtons() {
		List<MethodAction> acitons = getSubActionsRecursive();
		for (MethodAction action : acitons) {
			toolbar.add(action.createButton(true));
		}
	}






	
	

}
