package ru.dolganov.tool.knowledge.collector;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

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
		preInit(new MainController()).init(ui);
		preInit(new TreeController()).init(ui);
		preInit(new InfoController()).init(ui);
		preInit(new SnapshotController()).init(ui);
		
		//new RegController().init(ui.regPanel);
		//new TreeController().init(ui.treePanel);
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
	
	public static void find(String pckgname) {
        // Code from JWhich
        // ======
        // Translate the package name into an absolute path
        String name = new String(pckgname);
        if (!name.startsWith("/")) {
            name = "/" + name;
        }        
        name = name.replace('.','/');
        
        // Get a File object for the package
        URL url = Launcher.class.getResource(name);
        String filePath = url.getFile();
		File directory = new File(filePath);
        
		//from http://forums.sun.com/thread.jspa?threadID=341935, but throws java.lang.NullPointerException
        //directory = new File(Thread.currentThread().getContextClassLoader().getResource(filePath).getFile());
        
		// New code
        // ======
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String [] files = directory.list();
            for (int i=0;i<files.length;i++) {
                 
                // we are only interested in .class files
                if (files[i].endsWith(".class")) {
                    // removes the .class extension
                    String classname = files[i].substring(0,files[i].length()-6);
                    try {
                        // Try to create an instance of the object
                        String classPath = pckgname+"."+classname;
                        System.out.println(classPath);
						Object o = Class.forName(classPath).newInstance();
                        
                    } catch (ClassNotFoundException cnfex) {
                        System.err.println(cnfex);
                    } catch (InstantiationException iex) {
                        // We try to instantiate an interface
                        // or an object that does not have a 
                        // default constructor
                    	System.err.println(iex);
                    } catch (IllegalAccessException iaex) {
                        // The class is not public
                    	System.err.println(iaex);
                    }
                }
            }
        }
	}
}
