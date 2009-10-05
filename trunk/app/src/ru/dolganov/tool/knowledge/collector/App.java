package ru.dolganov.tool.knowledge.collector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import ru.chapaj.util.lang.ClassUtil;
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
	LinkedList<AppListener> listeners = new LinkedList<AppListener>();

	public DAO getDao() {
		return dao;
	}
	
	public void addListener(AppListener listener){
		listeners.add(listener);
	}
	
	public void fireAction(Object source, String action, Object... data){
		for(AppListener l : listeners) l.onAction(source, action, data);
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
		ui.jButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
			
		});
		ui.jButton1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				hideIfNeed();
			}
			
		});
		ui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				hideIfNeed();
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
						Class<?> dependenceClass =  ci.dependence();
						String dependenceClassName = dependenceClass.getName();
						if(dependenceClass.equals(Object.class)){
							initController(clazz, target);
							String name = clazz.getName();
							if(queue.containsKey(name)){
								for(CE ce : queue.get(name)){
									initController(ce.clazz, ce.target);
								}
								queue.remove(name);
							}
						}
						else if(!ClassUtil.isValid(dependenceClass, Controller.class)){
							throw new IllegalStateException("unknow controller's dependence class:"+dependenceClass);
						}
						else {
							if(Controller.controllers.containsKey(dependenceClassName)){
								initController(clazz, target);
							}
							else {
								ArrayList<CE> list = queue.get(dependenceClassName);
								if(list == null){
									list = new ArrayList<CE>();
									queue.put(dependenceClassName, list);
								}
								list.add(new CE(clazz, target));
							}
						}
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
	
//	public static void main(String[] args) {
//		System.out.println(TreeController.class
//				.isAssignableFrom(Controller.class));
//		System.out.println(Controller.class
//				.isAssignableFrom(TreeController.class));
//		System.out.println(ClassUtil.isValid(Controller.class,
//				TreeController.class));
//	}
	
	private static class CE {
		Class<?> clazz;
		Object target;
		public CE(Class<?> clazz, Object target) {
			super();
			this.clazz = clazz;
			this.target = target;
		}
	}
	private HashMap<String, ArrayList<CE>> queue = new HashMap<String, ArrayList<CE>>();
	private void initController(Class<?> clazz, Object target)
			throws InstantiationException, IllegalAccessException {
		Controller<?> c = (Controller<?>) clazz.newInstance();
		// System.out.println("init c:" + c);
		preInit(c).initUnsaveObject(target);
	}
	
	private <T> Controller<T> preInit(Controller<T> con){
		con.setDao(dao);
		con.setMainUI(ui);
		return con;
	}

	private void initUI() {
		ui = new MainWindow(null);
		ui.setTitle("Knowledge Collector");
//		ui.setIconImage(ImageBundle.getDefault().getAppIcon());
		ui.setLocationByPlatform(true);
	}

	public void exit() {
		System.exit(0);
	}
	
	public void show(){
		ui.setVisible(true);
		ui.toFront();
	}
	
	public void hideIfNeed() {
		if(ui.isActive()) ui.setVisible(false);	
	}
}
