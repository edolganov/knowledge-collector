package kc.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import ru.kc.platform.scripts.annotations.Mapping;
import ru.kc.platform.scripts.controller.ScriptController;
import ru.kc.platform.ui.tabbedform.MainForm;

@Mapping(MainForm.class)
public class SetSizeAndPosition extends ScriptController<MainForm>{
	
	@Override
	public void init() {
		ui.setTitle("Knowledge Collector");
		ui.setMinimumSize(new Dimension(1024, 540));
		
		ui.setSize(1024, 540);
		
        //position
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = ui.getSize();
        int windowX = Math.max(0, (screenSize.width - windowSize.width) / 2);
        int windowY = Math.max(0, (screenSize.height - windowSize.height) / 2);
        ui.setLocation(windowX, windowY); 
		
	}

}
