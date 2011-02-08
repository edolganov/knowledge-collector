package ru.kc.util.swing;

import javax.swing.SwingUtilities;

public class SwingUtil {
	
	public static void invokeInEDT(Runnable runnable){
		if(SwingUtilities.isEventDispatchThread()){
			runnable.run();
		} else {
			SwingUtilities.invokeLater(runnable);
		}
	}
}
