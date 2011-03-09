package ru.kc.platform.ui.tabs;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JTabbedPane;

public class TabbedWrapper {
	
	public static interface TabsListener {
		
		boolean canClose(Component comp, int index, String tabText);
		
		void onClosed(Component comp, int index, String tabText);
		
	}
	
	private JTabbedPane tabs;
	private ArrayList<TabHeader> headers = new ArrayList<TabHeader>();
	private CopyOnWriteArrayList<TabsListener> listeners = new CopyOnWriteArrayList<TabbedWrapper.TabsListener>();

	public TabbedWrapper(JTabbedPane tabs) {
		super();
		this.tabs = tabs;
	}

	
	public void addTab(Component comp, String text){
		addTab(tabs.getTabCount(), comp, text);
	}
	
	public void addTab(Component comp, String text, boolean canClose){
		addTab(tabs.getTabCount(), comp, text,canClose);
	}
	
	public void addTab(int index, Component comp, String text){
		addTab(index, comp, text, true);
	}
	
	public void addTab(int index, final Component comp, String text, boolean canClose){
		final TabHeader header = new TabHeader(text,canClose);
		tabs.add(comp,index);
		
		tabs.setTabComponentAt(index, header);
		headers.add(index, header);
		
		
		if(tabs.getTabCount() == 1){
			header.setSelected(true);
		} else {
			header.setSelected(false);
		}
		
		header.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!canClose(comp)) return;
				close(comp);
				
			}
		});
		
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

	public void setSelectedIndex(int index) {
		tabs.setSelectedIndex(index);
	}
	
	public void addListener(TabsListener l){
		listeners.add(l);
	}
	
	public void rename(Component comp, String text){
		int index = getIndex(comp);
		if(index == -1) return;
		headers.get(index).setText(text);
	}
	
	public void close(Component comp) {
		int index = getIndex(comp);
		if(index == -1) return;
		String text = headers.get(index).getText();
		
		tabs.remove(comp);
		headers.remove(index);
		for (TabsListener listener : listeners) listener.onClosed(comp,index,text);
	}
	
	
	public void setModified(Component comp, boolean value){
		int index = getIndex(comp);
		if(index == -1) return;
		headers.get(index).setModified(value);
	}
	
	
	private boolean canClose(Component comp) {
		int index = getIndex(comp);
		if(index == -1) return false;
		String text = headers.get(index).getText();
		
		for (TabsListener listener : listeners) {
			if(!listener.canClose(comp,index,text)) return false;
		}
		return true;
	}

	private void desellectAllHeaders() {
		for (TabHeader h : headers) h.setSelected(false);
		tabs.repaint();
	}

	private int getIndex(Component comp) {
		return tabs.indexOfComponent(comp);
	}


}
