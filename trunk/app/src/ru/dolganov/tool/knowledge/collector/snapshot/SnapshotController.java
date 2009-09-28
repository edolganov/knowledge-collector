package ru.dolganov.tool.knowledge.collector.snapshot;

import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;

public class SnapshotController extends Controller<MainWindow> {

	@Override
	public void init(MainWindow ui) {
		ui.snapTree.setVisible(false);
		
	}
	
	

}
