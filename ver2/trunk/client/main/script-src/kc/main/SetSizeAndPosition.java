package kc.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.scripts.controller.ScriptController;
import ru.kc.platform.ui.tabbedform.MainForm;

@Mapping(MainForm.class)
public class SetSizeAndPosition extends ScriptController<MainForm>{
	
	@Override
	public void init() {
		ui.setTitle("Knowledge Collector");
		
		//size
		int width = 1024;
		int height = 540;
		ui.setMinimumSize(new Dimension(560, 390));
		ui.setSize(width, height);
		
        //position
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = ui.getSize();
        int windowX = Math.max(0, (screenSize.width - windowSize.width) / 2);
        int windowY = Math.max(0, (screenSize.height - windowSize.height) / 2);
        ui.setLocation(windowX, windowY); 
		
	}

}
