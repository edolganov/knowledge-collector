package ru.kc.main.tab.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ru.kc.platform.common.event.CloseRequest;
import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.event.EventManager;

@SuppressWarnings("serial")
public class MainMenu extends JPopupMenu {

	JMenuItem exit = new JMenuItem("Exit");
	
	JMenuItem importItem = new JMenuItem("Import...");
	

	public MainMenu(final EventManager eventManager, final DomainMember member) {


		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				eventManager.fireEventInEDT(member, new CloseRequest());
			}

		});
		
		importItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});


	}

	@Override
	public void show(Component invoker, int x, int y) {
		removeAll();
		
		add(importItem);
		addSeparator();
		add(exit);

		super.show(invoker, x, y);
	}

}
