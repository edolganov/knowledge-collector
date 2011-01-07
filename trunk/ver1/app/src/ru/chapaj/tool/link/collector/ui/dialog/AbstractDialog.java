package ru.chapaj.tool.link.collector.ui.dialog;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public abstract class AbstractDialog extends JDialog {
	
	
	public boolean confirmedAction;
	
	protected JPanel uiRoot;
	
	public AbstractDialog(Dialog frame, String caption) {
		super(frame,caption,true);
		setLocationRelativeTo(frame);
		setResizable(false);
		confirmedAction = false;
		
        //Handle window closing correctly.
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        this.setContentPane(root);
        
        uiRoot = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.add(uiRoot);
        
        JButton ok = new JButton("ok");
        JPanel buttons = new JPanel();
        buttons.add(ok);
        
        root.add(buttons);
        
        ok.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				confirmedAction = true;
				AbstractDialog.this.dispose();
				
			}
        	
        });
	}

}
