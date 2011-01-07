package ru.chapaj.util.swing.menu;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class MenuUtil {
	
	public static void initCopyPastMenu(final JTextComponent component){
		component.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		if ( e.isPopupTrigger()) {
        	        // create popup menu and show 
        	        JTextComponent tc = (JTextComponent)component;
        	        JPopupMenu menu = new JPopupMenu(); 
        	        menu.add(new CutAction(tc)); 
        	        menu.add(new CopyAction(tc)); 
        	        menu.add(new PasteAction(tc)); 
        	        menu.add(new DeleteAction(tc)); 
        	        menu.addSeparator(); 
        	        menu.add(new SelectAllAction(tc)); 
        	        
        	        Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), tc);
        	        menu.show(tc, pt.x, pt.y);
        		}
        	}
		});
	}

}
