package ru.kc.util.swing.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;

public class DialogUtil {
	
	public static void addKeyAndContainerListenerRecursively(Component c,KeyListener keyListener){
        c.addKeyListener(keyListener);
        if(c instanceof Container) {
             Container cont = (Container)c;
             //cont.addContainerListener(new ContainerListener);
             Component[] children = cont.getComponents();
             for(int i = 0; i < children.length; i++){
                  addKeyAndContainerListenerRecursively(children[i],keyListener);
             }
        }
	}
	
	
	public static <T extends JDialog> T init(final T dialog){
		Window owner = dialog.getOwner();
		
		if(owner != null){
			dialog.setLocationRelativeTo(owner);
		}
		
		addKeyAndContainerListenerRecursively(dialog, new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
		          int code = e.getKeyCode();
		          if(code == KeyEvent.VK_ESCAPE){
		        	  dialog.setVisible(false);
		        	  dialog.dispose();
		          }
			}
		});
		
		return dialog;
	}
	
	
}
