package kc.main;

import javax.swing.JPanel;

import ru.kc.platform.scripts.annotations.Mapping;
import ru.kc.platform.scripts.controller.ScriptController;
import ru.kc.platform.ui.tabbedform.MainForm;
import ru.kc.platform.ui.tabbedform.TabbedPanel;
import ru.kc.platform.ui.tabs.TabbedWrapper;

@Mapping(MainForm.class)
public class InitTabs extends ScriptController<MainForm>{
	
	@Override
	public void init() {
		TabbedPanel root = ui.root;
		root.tabs.remove(root.testTab1);
		root.tabs.remove(root.testTab2);
		
		TabbedWrapper tabs = new TabbedWrapper(root.tabs);
		tabs.addTab(new JPanel(), "test tab");
		tabs.addTab(0,new JPanel(), "test tab 2");
		
	}

}
