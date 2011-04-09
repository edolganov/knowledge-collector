package ru.kc.main.tab;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JToolBar;

import ru.kc.common.controller.Controller;
import ru.kc.main.tab.tools.MainMenu;
import ru.kc.main.tab.ui.TabPanel;
import ru.kc.platform.action.AbstractAction;
import ru.kc.platform.action.ButtonAction;
import ru.kc.platform.action.ComboBoxAction;
import ru.kc.platform.action.facade.AbstractActionFacade;
import ru.kc.platform.action.facade.ButtonFacadeMediator;
import ru.kc.platform.action.facade.ComboBoxFacadeMediator;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.button.DropDownButton;
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
		MainMenu mainMenu = new MainMenu();
		DropDownButton dropDownButton = new DropDownButton();
		dropDownButton.setIcon(IconUtil.get("/ru/kc/common/img/mainMenu.png"));
		dropDownButton.setMenu(mainMenu);
		dropDownButton.setToolTipText("Main menu");
		toolbar.add(dropDownButton);
	}

	private void addSeparator() {
		toolbar.add(new JToolBar.Separator());
	}

	private void addExternalActionComponents() {
		List<AbstractActionFacade> facades = getSubActionFacades();
		AbstractActionFacade before = null;
		for (AbstractActionFacade facade : facades) {
			addSeparatorInNewGroup(before, facade);
			AbstractAction action = null;
			if(facade instanceof ButtonFacadeMediator){
				ButtonAction buttonAction = new ButtonAction();
				((ButtonFacadeMediator) facade).addRealFacade(buttonAction);
				action = buttonAction;
			}
			else if(facade instanceof ComboBoxFacadeMediator){
				ComboBoxAction comboAction = new ComboBoxAction();
				((ComboBoxFacadeMediator) facade).addRealFacade(comboAction);
				action = comboAction;
			}
			
			if(action != null){
				toolbar.add(action.getComponent());
			}
			before = facade;
		}
	}

	private void addSeparatorInNewGroup(AbstractActionFacade before,
			AbstractActionFacade next) {
		if(before != null){
			String beforeGroup = before.getGroup();
			String nextGroup = next.getGroup();
			if(beforeGroup == null) beforeGroup = "";
			if(nextGroup == null) nextGroup = "";
			if( ! beforeGroup.equals(nextGroup)){
				addSeparator();
			}
		}
	}






	
	

}
