package ru.kc.util.swing;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class SwingUtil {
	
	public static void invokeInEDT(Runnable runnable){
		if(SwingUtilities.isEventDispatchThread()){
			runnable.run();
		} else {
			SwingUtilities.invokeLater(runnable);
		}
	}
	
	
	public static void invokeInEDTAndWait(Runnable runnable) throws Throwable{
		if(SwingUtilities.isEventDispatchThread()){
			runnable.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(runnable);
				
			}catch (InvocationTargetException e) {
				throw e.getTargetException();
				
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
