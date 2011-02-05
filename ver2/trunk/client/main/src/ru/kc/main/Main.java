package ru.kc.main;

import java.io.File;

import ru.kc.platform.Platform;
import ru.kc.platform.app.App;
import ru.kc.platform.ui.tabbedform.MainForm;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		Platform.trySetSystemLookAndFeel();
		Platform.trySetNimbusLookAndFeel();
		Platform.setDataDir(new File("./data"));
		Platform.enableLogFile();
		
		App app = Platform.createApp();
		app.setRootUI(new MainForm());
		app.addScriptsDevDir(new File("./client/main/script-src"));
		app.addScriptsProdactionDir(new File("./data/scripts"));
		app.run();
		
	}

}
