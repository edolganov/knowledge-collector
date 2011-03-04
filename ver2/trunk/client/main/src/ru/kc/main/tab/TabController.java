package ru.kc.main.tab;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JToolBar;

import ru.kc.common.controller.Controller;
import ru.kc.main.tab.ui.TabPanel;
import ru.kc.platform.action.AbstractAction;
import ru.kc.platform.action.ButtonAction;
import ru.kc.platform.action.facade.AbstractActionFacade;
import ru.kc.platform.action.facade.ButtonFacadeMediator;
import ru.kc.platform.annotations.Mapping;
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
	

	public Component getComponent() {
		return component;
	}
	
	private void refreshToolbar() {
		toolbar.removeAll();
		
		addSystemButtons();
		addSeparator();
		addExternalActionComponents();
		

	}
	
	private void addSystemButtons() {
		JButton menu = new JButton(IconUtil.get("/ru/kc/common/img/mainMenu.png"));
		menu.setToolTipText("menu");
		toolbar.add(menu);
	}
	
	private void addSeparator() {
		toolbar.add(new JToolBar.Separator());
	}

	private void addExternalActionComponents() {
		List<AbstractActionFacade> facades = getSubActionFacades();
		for (AbstractActionFacade facade : facades) {
			AbstractAction action = null;
			if(facade instanceof ButtonFacadeMediator){
				ButtonAction buttonAction = new ButtonAction();
				((ButtonFacadeMediator) facade).addRealFacade(buttonAction);
				action = buttonAction;
			}
			
			if(action != null){
				toolbar.add(action.getComponent());
			}

		}
	}






	
	

}
