package ru.kc.module.tree.tools;

import java.awt.Component;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;

import ru.kc.main.model.NodeIcon;
import ru.kc.model.Node;
import ru.kc.module.tree.ui.CellPanel;
import ru.kc.util.Check;
import ru.kc.util.swing.keyboard.EnterKey;
import ru.kc.util.swing.tree.TreeFacade;

public class CellEditor implements TreeCellEditor {
	
	private TreeFacade treeFacade;
	private CellPanel cellPanel;
	private JTextField text;
	private JLabel label;
	private boolean enabledRequest;
	private boolean enabled;
	private ArrayList<CellEditorListener> customListeners = new ArrayList<CellEditorListener>();
	private ArrayList<CellEditorListener> swingListeners = new ArrayList<CellEditorListener>();
	
	public CellEditor(JTree tree) {
		treeFacade = new TreeFacade(tree);
		initRender();

	}

	private void initRender() {
		cellPanel = new CellPanel();
		cellPanel.setOpaque(false);
		text = cellPanel.textF;
		label = cellPanel.label;
		
		label.setText("");
		text.setText("");
		
		text.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction() {
				stopCellEditing();
			}
		});
	}
	
	public void setEnabledRequest() {
		this.enabledRequest = true;
	}

	@Override
	public String getCellEditorValue() {
		return text.getText();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return enabledRequest;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return enabledRequest;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void cancelCellEditing() {
		enabledRequest = false;
		enabled = false;
		
		for (CellEditorListener l : customListeners)l.editingCanceled(new ChangeEvent(this));
		for (CellEditorListener l : swingListeners)l.editingCanceled(new ChangeEvent(this));
	}

	@Override
	public boolean stopCellEditing() {
		String name = text.getText();
		boolean empty = Check.isEmpty(name);
		if(empty) return false;
		
		
		enabledRequest = false;
		enabled = false;
		
		for (CellEditorListener l : customListeners)l.editingStopped(new ChangeEvent(this));
		for (CellEditorListener l : swingListeners)l.editingCanceled(new ChangeEvent(this));
		return true;
	}


	public void addCustomListener(CellEditorListener l) {
		customListeners.add(l);
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		swingListeners.add(l);
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		swingListeners.remove(l);
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		Node node = treeFacade.getCurrentObject(Node.class);
		if(node == null) 
			throw new IllegalStateException("expected "+Node.class);
		label.setIcon(NodeIcon.getIcon(node));
		text.setText(node.getName());
		enabled = true;
		return cellPanel;
	}





}
