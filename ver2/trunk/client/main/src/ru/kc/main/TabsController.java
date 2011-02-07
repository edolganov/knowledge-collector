package ru.kc.main;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ru.kc.main.dashboard.DashboardModule;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.controller.AbstractController;
import ru.kc.platform.ui.tabbedform.MainForm;
import ru.kc.platform.ui.tabbedform.TabbedPanel;
import ru.kc.platform.ui.tabs.TabbedWrapper;
import ru.kc.platform.ui.tabs.TabbedWrapper.TabsListener;

@Mapping(MainForm.class)
public class TabsController extends Controller<MainForm> {

	@Override
	public void init() {
		TabbedPanel root = ui.root;
		root.tabs.remove(root.testTab1);
		root.tabs.remove(root.testTab2);
		
		TabbedWrapper tabs = new TabbedWrapper(root.tabs);
		tabs.addTab(new DashboardModule(), "dashboard", false);
		tabs.addTab(new JPanel(), "test tab");
		
		tabs.addListener(new TabsListener() {
			
			@Override
			public void onClosed(Component comp, int index, String text) {}
			
			@Override
			public boolean canClose(Component comp, int index, String text) {
				//default icon, custom title
				int n = JOptionPane.showConfirmDialog(
					ui,
				    "Close? "+text,
				    "???",
				    JOptionPane.YES_NO_OPTION);


				return n == 0;
			}
		});
		
	}

}
