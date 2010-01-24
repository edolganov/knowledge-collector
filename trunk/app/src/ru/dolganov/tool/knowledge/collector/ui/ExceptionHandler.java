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
		if(e instanceof NodeExistException){
			NodeExistException ex = (NodeExistException)e;
			final ErrorDialog errorDialog = new ErrorDialog();
			errorDialog.init(App.getDefault().getUI());
			errorDialog.ok.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					errorDialog.setConfirmedActionAndExit(true);
				}
			});
			
			
			errorDialog.label2.setText("Node '"+ex.getName()+"' already exist");
			errorDialog.setVisible(true);
		}
		else {
			log.error(e);
		}
		
	}

}
