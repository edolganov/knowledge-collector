package ru.kc.main;

import java.io.File;

import ru.kc.main.common.Context;
import ru.kc.main.event.ChildAdded;
import ru.kc.main.event.ChildDeletedRecursive;
import ru.kc.model.Node;
import ru.kc.platform.Platform;
import ru.kc.platform.app.App;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.event.EventManager;
import ru.kc.platform.ui.tabbedform.MainForm;
import ru.kc.tools.filepersist.InitParams;
import ru.kc.tools.filepersist.ServiceListener;
import ru.kc.tools.filepersist.impl.PersistServiceImpl;
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
		String rootControllersPackage = "ru.kc.main";
		
		Platform.setDataDir(dataDir);
		Platform.enableLogFile();
		
		App app = Platform.createApp();
		app.setRootUI(new MainForm());
		app.addScriptsDevDir(scriptsDevDir);
		app.addScriptsProdactionDir(scriptDir);
		app.addRootControllersPackage(rootControllersPackage);
		app.addContextData(context);
		app.init();
		initPersistEvents(app,context);
		app.run();
		
	}



	private static Context createContext(File knowDir) throws Exception {
		int maxNodesInContainer = 100;
		int maxContainerFilesInFolder = 100;
		int maxFoldersInLevel = 100;
		InitParams init = new InitParams(knowDir, maxNodesInContainer, maxContainerFilesInFolder, maxFoldersInLevel);
		PersistServiceImpl ps = new PersistServiceImpl();
		ps.init(init);
		
		return new Context(ps);
	}
	
	private static void initPersistEvents(App app, Context context) {
		AppContext appContext = app.getInitedContext();
		final EventManager eventManager = appContext.eventManager;
		context.persistService.addListener(new ServiceListener() {
			
			@Override
			public void onAdded(Node parent, Node child) {
				eventManager.fireEventInEDT(this,new ChildAdded(parent, child));
			}
			
			@Override
			public void onDeletedRecursive(Node parent, Node deletedChild) {
				eventManager.fireEventInEDT(this, new ChildDeletedRecursive(parent, deletedChild));
			}
		});
	}

}
