package ru.chapaj.util.app;

import javax.swing.UIManager;

public class GenericMain {
	
	protected static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
