package ru.kc.main;

import java.io.File;

import ru.kc.platform.Platform;
import ru.kc.platform.app.App;
import ru.kc.platform.ui.tabbedform.MainForm;
import ru.kc.tools.filepersist.InitParams;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.util.swing.laf.Laf;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		Laf.trySetSystemLookAndFeel();
		//Laf.trySetNimbusLookAndFeel();
		Laf.setupEnterActionForAllButtons();
		Laf.setupComboboxInputMap();
		
		File dataDir = new File("./data");
		File scriptDir = new File(dataDir,"scripts");
		File scriptsDevDir = new File("./client/main/script-src");
		File knowDir = new File(dataDir,"know");
		Context context = createContext(knowDir);
		
		Platform.setDataDir(dataDir);
		Platform.enableLogFile();
		
		App app = Platform.createApp();
		app.setRootUI(new MainForm());
		app.addScriptsDevDir(scriptsDevDir);
		app.addScriptsProdactionDir(scriptDir);
		app.addRootControllersPackage("ru.kc.main");
		app.addContextData(context);
		app.run();
		
	}

	private static Context createContext(File knowDir) throws Exception {
		int maxNodesInContainer = 100;
		int maxContainerFilesInFolder = 100;
		int maxFoldersInLevel = 100;
		InitParams init = new InitParams(knowDir, maxNodesInContainer, maxContainerFilesInFolder, maxFoldersInLevel);
		PersistService ps = new PersistService();
		ps.init(init);
		
		return new Context(ps);
	}

}
