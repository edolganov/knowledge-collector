package ru.chapaj.util.swing.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Сообщение о том, что идет перемещние элемента в списке через ctrl+up или ctrl+down
 * @author jenua.dolganov
 *
 */
public abstract class KeyUpDownAdapter extends KeyAdapter {
	
	public final static int ctrlMask = KeyEvent.CTRL_DOWN_MASK;
	
	@Override
	public void keyPressed(KeyEvent e) {
			if(! e.isConsumed() && e.getKeyCode() == KeyEvent.VK_UP && e.getModifiersEx() == ctrlMask){
				moveUp();
				e.consume();
			}
			else if(! e.isConsumed() && e.getKeyCode() == KeyEvent.VK_DOWN && e.getModifiersEx() == ctrlMask){
				moveDown();
				//System.out.println("moveDown");
				e.consume();
			}
	}
	
	public abstract void moveUp();
	
	public abstract void moveDown();

}
