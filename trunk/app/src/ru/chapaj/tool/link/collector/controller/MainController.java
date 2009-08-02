package ru.chapaj.tool.link.collector.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

import javax.swing.JFileChooser;

import ru.chapaj.tool.link.collector.App;
import ru.chapaj.tool.link.collector.store.file.DataContainerStore;
import ru.chapaj.tool.link.collector.ui.AppFrame;
import ru.chapaj.tool.link.collector.ui.dialog.SimpleConfirmDialog;

public class MainController extends Controller<AppFrame> {
	
	
	private DataContainerStore store;
	
	private JFileChooser fileChooser;
	private File curFile;
	
	public MainController() {
		store = new DataContainerStore();
		fileChooser = new JFileChooser();
	}
	
	@Override
	public void processProps(Properties props) {
		String lastFile = props.getProperty("last-file");
		if(lastFile != null) loadFile(new File(lastFile));
	}
	
	
	@Override
	public void init(final AppFrame ui) {
		ui.saveAs.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
		        int returnValue = fileChooser.showDialog(null,"Save");
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
			          saveFile(fileChooser.getSelectedFile(),true);
		        }
			}
			
		});
		
		ui.load.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
		        int returnValue = fileChooser.showDialog(null,"Load");
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
			          loadFile(fileChooser.getSelectedFile());
		        }
			}
			
		});
		
		ui.exitBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				App.getDefault().exit();
			}
			
		});
		
	}

	public void loadFile(File file){
		try {
			App.getDefault().reloadData(store.loadFile(file));
	  		curFile = file;
	  		App.getDefault().informer().newFile(file.getName());
	      }
	      catch(Exception ex){
	    	  ex.printStackTrace();
	    	  throw new IllegalStateException(ex);
	      }
	}
	
	public void actionSaveFile(boolean full){
		//actionSaveFile(App.getDefault().informer().isFileModified());
		actionSaveFile(true,full);
	}
	
//	public void actionSaveFileConform(){
//		actionSaveFile(SimpleConfirmDialog.confirm("Save before exit", "Save file before exit?"));
//	}
	
	private void actionSaveFile(boolean confirm,boolean full){
		if(curFile != null && confirm) saveFile(curFile,full);
	}
	

	
	private void saveFile(File file,boolean full){
		try {
			store.saveFile(file, App.getDefault().refreshData(full), true);
	        App.getDefault().informer().newFile(file.getName());
        }
        catch(Exception ex){
      	  ex.printStackTrace();
        }
	}

}
