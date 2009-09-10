package ru.chapaj.util.swing.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ContainerListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;

public class ExtendDialog extends JDialog {
	
	boolean confirmedAction;

	public boolean isConfirmedAction() {
		return confirmedAction;
	}

	public void setConfirmedAction(boolean confirmedAction) {
		this.confirmedAction = confirmedAction;
	}
	
	public void setConfirmedActionAndExit(boolean confirmedAction) {
		setConfirmedAction(confirmedAction);
		setVisible(false);
		
	}
	

	public ExtendDialog() {
		super();
	}

	public ExtendDialog(Dialog owner, boolean modal) {
		super(owner, modal);
	}

	public ExtendDialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
	}

	public ExtendDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public ExtendDialog(Dialog owner, String title) {
		super(owner, title);
	}

	public ExtendDialog(Dialog owner) {
		super(owner);
	}

	public ExtendDialog(Frame owner, boolean modal) {
		super(owner, modal);
	}

	public ExtendDialog(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
	}

	public ExtendDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public ExtendDialog(Frame owner, String title) {
		super(owner, title);
	}

	public ExtendDialog(Frame owner) {
		super(owner);
	}

	public ExtendDialog(Window owner, ModalityType modalityType) {
		super(owner, modalityType);
	}

	public ExtendDialog(Window owner, String title, ModalityType modalityType,
			GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
	}

	public ExtendDialog(Window owner, String title, ModalityType modalityType) {
		super(owner, title, modalityType);
	}

	public ExtendDialog(Window owner, String title) {
		super(owner, title);
	}

	public ExtendDialog(Window owner) {
		super(owner);
	}
	
	public void init(Component owner){
		setLocationRelativeTo(owner);
		setModal(true);
		setResizable(false);
		addKeyAndContainerListenerRecursively(this, new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
		          int code = e.getKeyCode();
		          if(code == KeyEvent.VK_ESCAPE){
		        	  ExtendDialog.this.setConfirmedActionAndExit(false);
		          }
			}
		});
	}
	
	
	public void remove(){
		removeAll();
		dispose();
	}
	
    private void addKeyAndContainerListenerRecursively(Component c,KeyListener keyListener)
    {
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
	
	

}
