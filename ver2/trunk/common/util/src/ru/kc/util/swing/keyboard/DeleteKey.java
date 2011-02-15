package ru.kc.util.swing.keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class DeleteKey extends KeyAdapter{
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DELETE){
			doAction(null);
		}
	}
	
	protected abstract void doAction(KeyEvent e);
}
