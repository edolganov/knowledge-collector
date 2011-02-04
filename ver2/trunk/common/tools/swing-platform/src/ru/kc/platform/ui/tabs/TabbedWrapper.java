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
		addTab(index, comp, text, true);
	}
	
	public void addTab(int index,Component comp, String text, boolean canClose){
		final TabHeader header = new TabHeader(text,canClose);
		tabs.add(comp,index);
		
		tabs.setTabComponentAt(index, header);
		headers.add(index, header);
		
		
		if(tabs.getTabCount() == 1){
			header.setSelected(true);
		}
		
		comp.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				header.setSelected(true);
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				desellectAllHeaders();
			}
			
			@Override
			public void componentResized(ComponentEvent e) {}
			
			@Override
			public void componentMoved(ComponentEvent e) {}
			

		});
	}

	private void desellectAllHeaders() {
		for (TabHeader h : headers) h.setSelected(false);
	}

}
