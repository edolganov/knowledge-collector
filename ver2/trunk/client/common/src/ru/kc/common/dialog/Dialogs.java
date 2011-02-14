package ru.kc.common.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

public class Dialogs {
	
	public static boolean confirmByDialog(Component parent){
		int n = confirmByDialog(parent, "Подтверждение операции", "Подтвердить операцию?", 1, "Ок", "Отмена");
		return n == 0;
	}
	public static boolean confirmByDialog(Component parent,String text){
		int n = confirmByDialog(parent, "Подтверждение операции", text, 1, "Ок", "Отмена");
		return n == 0;
	}
	
	public static boolean confirmByDialog(Component parent, String title, String text){
		int n = confirmByDialog(parent, title, text, 1, "Ок", "Отмена");
		return n == 0;
	}
	
	public static int confirmByDialog(Component parent, String title, String text, int initialOption, Object... options){
		if(options == null){
			options = new String[]{"Ок", "Отмена"};
			initialOption = 1;
		}
		

		int n = JOptionPane.showOptionDialog(parent,
			text,
		    title,
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[initialOption]);
		return n;
		
	}
	
	
	
	
	
	
	public static void errorDialog(Component parent, Object message){
		errorDialog(parent, "Системная ошибка",message);
	}
	
	public static void errorDialog(Component parent, String title, Object message){
		showDialog(parent, title, message, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void warnDialog(Component parent, Object message) {
		warnDialog(parent,"Внимание",message);
	}
	
	public static void warnDialog(Component parent, String title, Object message) {
		showDialog(parent, title, message, JOptionPane.WARNING_MESSAGE);
	}
	
	public static void infoDialog(Component parent, Object message){
		infoDialog(parent, "",message);
	}
	
	public static void infoDialog(Component parent, String title, Object message){
		showDialog(parent, title, message, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showDialog(Component parent, String title, Object message, int optionPane) {
		JOptionPane.showMessageDialog(parent,
				message,
			    title,
			    optionPane);
	}

}
