package ru.dolganov.tool.knowledge.collector.main;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.chapaj.util.swing.IconHelper;
import ru.dolganov.tool.knowledge.collector.Controller;

public class MainController extends Controller<MainWindow> {

	@Override
	public void init(MainWindow ui) {
		ui.saveB.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				dao.flushMeta();
			}
			
		});
		
		ui.saveB.setEnabled(false);
		
		ui.dirB.setIcon(IconHelper.get("/images/kc/app/dir.png"));
		ui.linkB.setIcon(IconHelper.get("/images/kc/app/netLink.png"));
		ui.noteB.setIcon(IconHelper.get("/images/kc/app/note.png"));
	}
	
	

}
