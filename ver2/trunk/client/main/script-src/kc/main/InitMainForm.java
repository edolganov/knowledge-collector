package kc.main;

import ru.kc.platform.main.ui.MainForm;
import ru.kc.platform.scripts.annotations.Mapping;
import ru.kc.platform.scripts.controller.ScriptController;

@Mapping(MainForm.class)
public class InitMainForm extends ScriptController<MainForm>{
	
	@Override
	public void init() {
		ui.menu.setVisible(false);
		ui.root.header.setVisible(false);
		ui.root.footer.setVisible(true);
		
	}

}
