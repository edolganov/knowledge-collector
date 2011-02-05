package ru.kc.platform;

import java.awt.Color;
import java.io.File;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import ru.kc.platform.app.App;
import ru.kc.util.log4j.Log4JConfig;

public class Platform {
	
	private static File dataDir;


	public static void setDataDir(File dataDir) {
		Platform.dataDir = dataDir;
		dataDir.mkdir();
	}


	public static void enableLogFile() throws Exception {
		String fileName = "app.log";
		File logFile = new File(dataDir,fileName);
		logFile.delete();
		Log4JConfig.defaultConfig(Log4JConfig.JBOSS_PATTERN,logFile.getPath());
	}


	public static App createApp() {
		return new App();
	}

	
	

	public static void trySetSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static void trySetNimbusLookAndFeel() {
		try {
			UIManager.put("control", new Color(212, 208, 200));
//			UIManager.put("nimbusBase", new Color(129, 136, 143));
////			UIManager.put("nimbusBase", new Color(115, 163, 212));
//			UIManager.put("nimbusFocus", new Color(28, 28, 31));
//			UIManager.put("nimbusInfoBlue", new Color(28, 30, 33));
////			UIManager.put("nimbusSelectionBackground", new Color(61, 63, 68));
			UIManager.put("nimbusSelectionBackground", new Color(10, 35, 107));
	        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	            if ("Nimbus".equals(info.getName())) {
	            	UIManager.setLookAndFeel(info.getClassName());
	            	break;
	            }
	        }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static void trySetSeaGlassLookAndFeel() {
		try {
		    UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static void trySetJTattooLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}



}
