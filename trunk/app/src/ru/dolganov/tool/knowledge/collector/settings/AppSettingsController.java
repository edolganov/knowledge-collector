package ru.dolganov.tool.knowledge.collector.settings;

import java.io.File;
import java.util.Properties;

import ru.chapaj.util.store.XmlStore;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.chapaj.util.xml.ObjectToXMLConverter;
import ru.dolganov.tool.knowledge.collector.App;
import ru.dolganov.tool.knowledge.collector.AppListener;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;
import ru.dolganov.tool.knowledge.collector.ui.ExceptionHandler;

@ControllerInfo(target=MainWindow.class)
public class AppSettingsController extends Controller<MainWindow> {

	
	MainWindow ui;
	ExtendTree tree;
	SettingsMap settings;
	
	File dir = new File("./settings/");
	File file = null;
	String magicFileName;
	
	
	XmlStore<SettingsMap> store = new XmlStore<SettingsMap>() {
		
		@Override
		protected void config(ObjectToXMLConverter<SettingsMap> converter) {
			converter.configureAliases(SettingsMap.class);
		}
	};
	
	@Override
	public void init(MainWindow ui_) {
		ui = ui_;
		App.getDefault().addListener(new AppListener(){

			@Override
			public void onAction(Object source, String action, Object... data_) {
				if("exit".equals(action)){
					save();
				}
			}
			
		});
		
		load();
		
	}
	
	private void load() {
		dir.mkdir();
		
		try {
			file = initFile();
			if(!file.exists()) return;
			settings = store.loadFile(file);
			
			if(settings != null){
				loadPosition();
			}
			
			
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		
	}

	private File initFile() {
		Properties systemProps = System.getProperties();
		File[] roots = File.listRoots();
		if(roots == null) roots = new File[0];
		
		//специфичное для данного компьютера имя
		magicFileName = "" 
		 + "--"
		 + systemProps.getProperty("os.name")
		 + "--"
		 + Runtime.getRuntime().availableProcessors()
		 + "--"
		 + (roots.length > 0 ? ""+roots[0].getTotalSpace() : systemProps.getProperty("java.io.tmpdir"));
		
		return new File("./settings/comp-"+magicFileName.hashCode()+".xml");
	}

	public void save(){
		try {
			if(settings == null){
				settings = new SettingsMap();
			}
			settings.map().put("cur-system", magicFileName);
			
			savePosition();
			
			store.saveFile(file, settings, false);
			
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		
	}
	
	private void loadPosition() {
		try {
			String width = settings.map().get("ui-width");
			String height = settings.map().get("ui-height");
			String x = settings.map().get("ui-x");
			String y = settings.map().get("ui-y");
			ui.setBounds(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(width), Integer.parseInt(height));
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

	private void savePosition() {
		try {
			settings.map().put("ui-width",""+ui.getWidth());
			settings.map().put("ui-height",""+ui.getHeight());
			settings.map().put("ui-x",""+ui.getX());
			settings.map().put("ui-y",""+ui.getY());
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		
	}

	
	

}
