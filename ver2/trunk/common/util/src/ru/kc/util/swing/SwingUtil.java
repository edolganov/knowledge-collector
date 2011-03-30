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
	
	
	public static void invokeInEDTAndWait(Runnable runnable) throws Exception{
		if(SwingUtilities.isEventDispatchThread()){
			runnable.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(runnable);
				
			}catch (InvocationTargetException e) {
				Throwable targetException = e.getTargetException();
				if(targetException instanceof Exception)
					throw (Exception) targetException;
				if(targetException instanceof Error)
					throw (Error)targetException;
				throw new IllegalStateException(targetException);
				
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
