package ru.kc.main;

import java.io.File;

import ru.kc.platform.Platform;
import ru.kc.platform.app.App;
import ru.kc.platform.ui.tabbedform.MainForm;
import ru.kc.util.swing.laf.Laf;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		Laf.trySetSystemLookAndFeel();
		//Laf.trySetNimbusLookAndFeel();
		//Laf.setupEnterActionForAllButtons();
		//Laf.setupComboboxInputMap();
		
		Platform.setDataDir(new File("./data"));
		Platform.enableLogFile();
		
		App app = Platform.createApp();
		app.setRootUI(new MainForm());
		app.addScriptsDevDir(new File("./client/main/script-src"));
		app.addScriptsProdactionDir(new File("./data/scripts"));
		app.addRootControllersPackage("ru.kc.main");
		app.run();
		
	}

}
