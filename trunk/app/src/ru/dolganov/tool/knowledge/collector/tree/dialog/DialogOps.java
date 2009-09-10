package ru.dolganov.tool.knowledge.collector.tree.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.chapaj.util.Check;
import ru.chapaj.util.swing.listener.KeyEnterAdapter;
import ru.dolganov.tool.knowledge.collector.App;
import model.knowledge.Dir;

public class DialogOps {
	
	public static Dir newDir(){
		Dir out = null;
		
		final NewDirDialog newDir = new NewDirDialog();
		newDir.init(App.getDefault().getUI());
		
		newDir.ok.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				newDir.setConfirmedActionAndExit(true);
			}
		});
		
		newDir.namePanel.textField.addKeyListener(new KeyEnterAdapter(){

			@Override
			public void onEnter() {
				newDir.setConfirmedActionAndExit(true);
			}
			
		});
		
		newDir.setVisible(true);
		
		if(newDir.isConfirmedAction()){
			String name = newDir.namePanel.textField.getText();
			if(!Check.isEmpty(name)){
				out = new Dir();
				out.setName(name);
			}
		}
		newDir.remove();
		return out;
	}

}
