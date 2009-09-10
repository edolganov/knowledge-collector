package ru.chapaj.util.swing.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public abstract class KeyEnterAdapter extends KeyAdapter {
	
	
	@Override
	public void keyPressed(KeyEvent e) {
			if(! e.isConsumed() && e.getKeyCode() == KeyEvent.VK_ENTER){
				onEnter();
				e.consume();
			}
	}
	
	public abstract void onEnter();
	

}
