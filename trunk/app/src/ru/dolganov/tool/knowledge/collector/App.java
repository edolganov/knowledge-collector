package ru.dolganov.tool.knowledge.collector;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

import ru.chapaj.util.lang.PackageExplorer;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.dao.DAO;
import ru.dolganov.tool.knowledge.collector.dao.fs.FSDAOImpl;
import ru.dolganov.tool.knowledge.collector.info.InfoController;
import ru.dolganov.tool.knowledge.collector.main.MainController;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;
import ru.dolganov.tool.knowledge.collector.snapshot.SnapshotController;
import ru.dolganov.tool.knowledge.collector.tree.TreeController;
import sun.misc.Launcher;

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
	
	public <N extends Controller<?>> N getController(Class<N> clazz){
		return Controller.get(clazz);
	}

	public void init() {
		dao = new FSDAOImpl("./know");
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
		PackageExplorer.find("ru.dolganov", new PackageExplorer.Callback(){

			@Override
			public void found(Class<?> clazz) {
				ControllerInfo ci = clazz.getAnnotation(ControllerInfo.class);
				if(ci != null){
					try {
						Class<?> targetClass = ci.target();
						if(targetClass == null) throw new IllegalStateException("null target of controller:"+clazz);
						Object target = null;
						if(targetClass.equals(MainWindow.class)){
							target = ui;
						}
						else {
							throw new IllegalStateException("unknow controller's target:"+targetClass);
						}
						Controller<?> c = (Controller<?>)clazz.newInstance();
						preInit(c).initUnsaveObject(target);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		});
//		preInit(new MainController()).init(ui);
//		preInit(new TreeController()).init(ui);
//		preInit(new InfoController()).init(ui);
//		preInit(new SnapshotController()).init(ui);
		
	}
	
	private <T> Controller<T> preInit(Controller<T> con){
		con.setDao(dao);
		con.setMainUI(ui);
		return con;
	}

	private void initUI() {
		ui = new MainWindow(null);
//		ui.setIconImage(ImageBundle.getDefault().getAppIcon());
		ui.setLocationByPlatform(true);
	}
}
