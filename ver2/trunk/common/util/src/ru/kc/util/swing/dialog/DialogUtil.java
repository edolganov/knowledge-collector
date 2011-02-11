package ru.kc.util.swing.dialog;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

public class DialogUtil {
	
	
	public static <T extends JDialog> T init(final T dialog){
		Window owner = dialog.getOwner();
		
		if(owner != null){
			dialog.setLocationRelativeTo(owner);
		}
		addEscapeListener(dialog);
		
		return dialog;
	}
	
	public static void addEscapeListener(final JDialog dialog) {
	    ActionListener escListener = new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            dialog.dispose();
	        }
	    };

	    dialog.getRootPane().registerKeyboardAction(escListener,
	            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	            JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	
	
}
