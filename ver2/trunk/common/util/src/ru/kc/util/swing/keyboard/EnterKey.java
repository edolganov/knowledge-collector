package ru.kc.util.swing.keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class EnterKey extends KeyAdapter{
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			doAction();
		}
	}
	
	protected abstract void doAction();
}
