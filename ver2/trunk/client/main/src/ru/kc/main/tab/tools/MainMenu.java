package ru.kc.main.tab.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.common.Context;
import ru.kc.common.imports.event.ImportOldClientDataRequest;
import ru.kc.common.tree.TreeService;
import ru.kc.common.tree.event.GetTreeServiceRequest;
import ru.kc.model.Node;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.common.event.CloseRequest;
import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.event.EventManager;
import ru.kc.tools.filepersist.PersistService;

@SuppressWarnings("serial")
public class MainMenu extends JPopupMenu {
	
	private static final Log log = LogFactory.getLog(MainMenu.class);

	JMenuItem exit = new JMenuItem("Exit");
	
	JMenuItem importItem = new JMenuItem("Import...");
	

	public MainMenu(AppContext appContext, Context context, final DomainMember member) {

		final EventManager eventManager = appContext.eventManager;
		final PersistService persistService = context.persistService;
		
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				eventManager.fireEventInEDT(member, new CloseRequest());
			}

		});
		
		importItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node importRoot = null;
				try {
					TreeService treeService = eventManager.fireRequestInEDT(member, new GetTreeServiceRequest());
					importRoot = treeService.getRoot().getUserObject(Node.class);
				} catch (IllegalStateException ex) {
					log.info("no tree service found");
				} catch (Exception ex) {
					log.error("", ex);
					return;
				}
				
				if(importRoot == null){
					try {
						importRoot = persistService.tree().getRoot();
					} catch (Exception ex) {
						log.error("", ex);
						return;
					}
				}
				
				eventManager.fireSaveRequestInEDT(member, new ImportOldClientDataRequest(importRoot));
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
