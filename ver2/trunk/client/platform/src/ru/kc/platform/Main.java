package ru.kc.platform;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ru.kc.platform.main.App;
import ru.kc.util.log4j.Log4JConfig;

public class Main {
	
	public static void main(String[] args) throws Exception {
		setupLaf();
		setupLog();
		runApp();
	}


	private static void setupLaf() throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
	
	private static void setupLog() throws IOException {
		File dir = new File("./data");
		dir.mkdir();
		
		String logFileName = "./data/client.log";
		new File(logFileName).delete();
		Log4JConfig.defaultConfig(Log4JConfig.JBOSS_PATTERN,logFileName);
	}
	
	private static void runApp() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					App app = new App();
					app.init();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
