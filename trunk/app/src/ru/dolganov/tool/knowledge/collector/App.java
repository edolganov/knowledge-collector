package ru.dolganov.tool.knowledge.collector;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ru.dolganov.tool.knowledge.collector.main.MainWindow;

public class App {
	
	private static App def;
	
	public static App getDefault(){
		if(def == null) def = new App();
		return def;
	}
	
	MainWindow ui;

	public MainWindow getUI() {
		return ui;
	}

	public void init() {
		initUI();
		initControllers();
		
		ui.setVisible(true);
		ui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
	}

	private void initControllers() {
		//new RegController().init(ui.regPanel);
		//new TreeController().init(ui.treePanel);
	}

	private void initUI() {
		ui = new MainWindow(null);
//		ui.setIconImage(ImageBundle.getDefault().getAppIcon());
		ui.setLocationByPlatform(true);
	}
}
