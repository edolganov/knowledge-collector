package ru.kc.platform.ui.tabs;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JTabbedPane;

public class TabbedWrapper {
	
	JTabbedPane tabs;
	ArrayList<TabHeader> headers = new ArrayList<TabHeader>();
	TabHeader selected;

	public TabbedWrapper(JTabbedPane tabs) {
		super();
		this.tabs = tabs;
	}
	
	public void addTab(Component comp, String text){
		addTab(tabs.getTabCount(), comp, text);
	}
	
	public void addTab(int index,Component comp, String text){
		final TabHeader tabHeader = new TabHeader(text);
		tabs.add(comp,index);
		tabs.setTabComponentAt(index, tabHeader);
		headers.add(index, tabHeader);
		
//		if(tabs.getTabCount() == 1){
//			button.showButton();
//		} else {
//			button.hideButton();
//		}
		
//		comp.addComponentListener(new ComponentListener() {
//			
//			@Override
//			public void componentShown(ComponentEvent e) {
//				hideAllButtons();
//				button.showButton();
//			}
//			
//			@Override
//			public void componentHidden(ComponentEvent e) {
//				hideAllButtons();
//				button.hideButton();
//			}
//			
//			@Override
//			public void componentResized(ComponentEvent e) {}
//			
//			@Override
//			public void componentMoved(ComponentEvent e) {}
//			
//
//		});
	}

//	private void hideAllButtons() {
//		for (TabButton b : buttons) b.hideButton();
//	}

}
