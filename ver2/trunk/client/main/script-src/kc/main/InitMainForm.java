package kc.main;

import ru.kc.platform.controller.annotations.Mapping;
import ru.kc.platform.scripts.controller.ScriptController;
import ru.kc.platform.ui.tabbedform.MainForm;

@Mapping(MainForm.class)
public class InitMainForm extends ScriptController<MainForm>{
	
	@Override
	public void init() {
		ui.menu.setVisible(false);
		ui.root.header.setVisible(false);
		ui.root.footer.setVisible(false);
		
	}

}
