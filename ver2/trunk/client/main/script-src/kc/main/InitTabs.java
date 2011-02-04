package kc.main;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ru.kc.platform.scripts.annotations.Mapping;
import ru.kc.platform.scripts.controller.ScriptController;
import ru.kc.platform.ui.tabbedform.MainForm;
import ru.kc.platform.ui.tabbedform.TabbedPanel;
import ru.kc.platform.ui.tabs.TabbedWrapper;
import ru.kc.platform.ui.tabs.TabbedWrapper.TabsListener;

@Mapping(MainForm.class)
public class InitTabs extends ScriptController<MainForm>{
	
	@Override
	public void init() {
		TabbedPanel root = ui.root;
		root.tabs.remove(root.testTab1);
		root.tabs.remove(root.testTab2);
		
		TabbedWrapper tabs = new TabbedWrapper(root.tabs);
		tabs.addTab(new JPanel(), "test tab 1");
		tabs.addTab(new JPanel(), "test tab 2");
		tabs.addTab(new JPanel(), "test tab 3");
		tabs.addTab(new JPanel(), "test tab 4");
		tabs.addTab(new JPanel(), "test tab 5");
		tabs.addTab(new JPanel(), "test tab 6");
		tabs.addTab(0,new JPanel(), "test tab 0",false);
		tabs.setSelectedIndex(0);
		
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
