package ru.kc.platform.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractAction {
	
	private static final Log log = LogFactory.getLog(AbstractAction.class);
	
	public AbstractAction() {
		super();
	}
	
	
	public JButton createButton(){
		return createButton(false);
	}
	
	public JButton createButton(boolean textInToolTip){
		JButton button = new JButton();
		if(!textInToolTip){
			button.setText(getText());
		} else {
			button.setToolTipText(getText());
		}
		button.setIcon(getIcon());
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					AbstractAction.this.actionPerformed();
				}catch (Exception ex) {
					log.error(e);
				}
			}
		});
		return button;
	}

	
	protected abstract String getText();
	
	protected abstract Icon getIcon();
	
	protected abstract void actionPerformed() throws Exception;

	

	
}
