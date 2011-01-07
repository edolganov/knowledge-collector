package ru.chapaj.tool.link.collector;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import ru.chapaj.util.swing.GlobalHotkeyManager;

public class GlobalHotKeyListener {
	
	LinkedList<Object> passActionStack;
	
	public static abstract class KeyAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(final ActionEvent e) {
			if(App.getDefault().canUseGlobalKeys()){
				new Thread("global-key-"+System.nanoTime()){
					@Override
					public void run() {
						actionPerformed_(e);
					}
				}.start();
				
			}
		}
		
		protected abstract void actionPerformed_(ActionEvent e);
		
	}
	
	public void passAction(){
		synchronized (passActionStack) {
			passActionStack.push(null);
		}
	}
	
//	private boolean canDoAction
	
	public GlobalHotKeyListener() {
		passActionStack = new LinkedList<Object>();
		GlobalHotkeyManager hotkeyManager = GlobalHotkeyManager.getInstance();
		String key = "deleteNode";
		hotkeyManager.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, 0, false), key);
		hotkeyManager.getActionMap().put(key, new KeyAction(){

			@Override
			public void actionPerformed_(ActionEvent e) {
				setEnabled(false);
				App.getDefault().getTreeController().actionDeleteNode();
				setEnabled(true);
				
			}
			
		});
		
//		key = "saveFile";
//		hotkeyManager.getInputMap().put(KeyStroke.getKeyStroke(
//				KeyEvent.VK_S, KeyEvent.CTRL_MASK, false), key);
//		hotkeyManager.getActionMap().put(key, new KeyAction(){
//
//			@Override
//			public void actionPerformed_(ActionEvent e) {
//				setEnabled(false);
//				App.getDefault().getMainController().actionSaveFile();
//				setEnabled(true);
//				
//			}
//			
//		});
		
		key = "createDir";
		hotkeyManager.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_X, KeyEvent.SHIFT_MASK, true), key);
		hotkeyManager.getActionMap().put(key, new KeyAction(){

			@Override
			public void actionPerformed_(ActionEvent e) {
				setEnabled(false);
				App.getDefault().getTreeController().actionCreateDir();
				setEnabled(true);
				
			}
			
		});
		
		key = "createLink";
		hotkeyManager.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_C, KeyEvent.SHIFT_MASK, true), key);
		hotkeyManager.getActionMap().put(key, new KeyAction(){

			@Override
			public void actionPerformed_(ActionEvent e) {
				setEnabled(false);
				App.getDefault().getTreeController().actionCreateLink();
				setEnabled(true);
				
			}
			
		});
		
		key = "hideApp";
		hotkeyManager.getInputMap().put(KeyStroke.getKeyStroke(
				KeyEvent.VK_ESCAPE, 0, true), key);
		hotkeyManager.getActionMap().put(key, new KeyAction(){

			@Override
			public void actionPerformed_(ActionEvent e) {
				setEnabled(false);
				App.getDefault().hideIfNeed();
				setEnabled(true);
				
			}
			
		});
		
		
	}

}
