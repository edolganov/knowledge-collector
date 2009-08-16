package ru.dolganov.tool.knowledge.collector;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ru.dolganov.tool.knowledge.collector.dao.DAO;
import ru.dolganov.tool.knowledge.collector.dao.DefaultDAOImpl;
import ru.dolganov.tool.knowledge.collector.info.InfoController;
import ru.dolganov.tool.knowledge.collector.main.MainController;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;
import ru.dolganov.tool.knowledge.collector.tree.TreeController;

public class App {
	
	private static App def;
	
	public static App getDefault(){
		if(def == null) def = new App();
		return def;
	}
	
	MainWindow ui;
	DAO dao;

	public DAO getDao() {
		return dao;
	}

	public MainWindow getUI() {
		return ui;
	}

	public void init() {
		dao = new DefaultDAOImpl("./know/data.xml");
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
		preInit(new MainController()).init(ui);
		preInit(new TreeController()).init(ui);
		preInit(new InfoController()).init(ui);
		
		//new RegController().init(ui.regPanel);
		//new TreeController().init(ui.treePanel);
	}
	
	private <T> Controller<T> preInit(Controller<T> con){
		con.setDao(dao);
		return con;
	}

	private void initUI() {
		ui = new MainWindow(null);
//		ui.setIconImage(ImageBundle.getDefault().getAppIcon());
		ui.setLocationByPlatform(true);
	}
}
