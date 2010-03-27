package ru.dolganov.tool.knowledge.collector.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.chapaj.util.log.Log;
import ru.dolganov.tool.knowledge.collector.App;
import ru.dolganov.tool.knowledge.collector.dao.exception.NodeExistException;
import ru.dolganov.tool.knowledge.collector.dialog.ErrorDialog;

public class ExceptionHandler {
	
	static final Log log = Log.getInstance(ExceptionHandler.class);
	
	public static void handle(Throwable e){

		
		String msg = null;
		
		if(e instanceof NodeExistException){
			NodeExistException ex = (NodeExistException)e;
			msg = "Node '"+ex.getName()+"' already exist";
		}
		else {
			msg = "System error: "+e.getMessage();
			log.error(e);
		}
		
		if(msg != null){
			final ErrorDialog errorDialog = new ErrorDialog();
			errorDialog.init(App.getDefault().getUI());
			errorDialog.ok.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					errorDialog.setConfirmedActionAndExit(true);
				}
			});
			
			errorDialog.label2.setText(msg);
			errorDialog.setVisible(true);
		}
		

		
	}

}
