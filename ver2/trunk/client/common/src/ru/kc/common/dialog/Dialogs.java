package ru.kc.common.dialog;

import java.awt.Component;

import javax.swing.JDialog;

import ru.kc.util.swing.config.ComponentScanner;



public class Dialogs {
	
	
	public void init(final ComponentScanner componentScanner){
		JOptionPaneExt.setListener(new JOptionPaneExt.InitListener() {
			
			@Override
			public void onInit(JDialog dilaog) {
				componentScanner.scanAndInit(dilaog);
			}
		});
		
	}
	
	
	
	
	public boolean confirmByDialog(Component parent){
		int n = confirmByDialog(parent, "Confirm the operation", "Confirm the operation?", 1, "OK", "Cancel");
		return n == 0;
	}
	public boolean confirmByDialog(Component parent,String text){
		int n = confirmByDialog(parent, "Confirm the operation", text, 1, "OK", "Cancel");
		return n == 0;
	}
	
	public boolean confirmByDialog(Component parent, String title, String text){
		int n = confirmByDialog(parent, title, text, 1, "OK", "Cancel");
		return n == 0;
	}
	
	public int confirmByDialog(Component parent, String title, String text, int initialOption, Object... options){
		if(options == null){
			options = new String[]{"OK", "Cancel"};
			initialOption = 1;
		}
		

		int n = JOptionPaneExt.showOptionDialog(parent,
			text,
		    title,
		    JOptionPaneExt.YES_NO_CANCEL_OPTION,
		    JOptionPaneExt.QUESTION_MESSAGE,
		    null,
		    options,
		    options[initialOption]);
		return n;
		
	}
	
	
	
	
	
	
	public void errorDialog(Component parent, Object message){
		errorDialog(parent, "System Error",message);
	}
	
	public void errorDialog(Component parent, String title, Object message){
		showDialog(parent, title, message, JOptionPaneExt.ERROR_MESSAGE);
	}
	
	public void warnDialog(Component parent, Object message) {
		warnDialog(parent,"Warning",message);
	}
	
	public void warnDialog(Component parent, String title, Object message) {
		showDialog(parent, title, message, JOptionPaneExt.WARNING_MESSAGE);
	}
	
	public void infoDialog(Component parent, Object message){
		infoDialog(parent, "",message);
	}
	
	public void infoDialog(Component parent, String title, Object message){
		showDialog(parent, title, message, JOptionPaneExt.INFORMATION_MESSAGE);
	}
	
	public void showDialog(Component parent, String title, Object message, int optionPane) {
		JOptionPaneExt.showMessageDialog(parent,
				message,
			    title,
			    optionPane);
	}

}
