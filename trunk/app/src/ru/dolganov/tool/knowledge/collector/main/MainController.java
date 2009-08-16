package ru.dolganov.tool.knowledge.collector.main;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
		
	}
	
	

}
