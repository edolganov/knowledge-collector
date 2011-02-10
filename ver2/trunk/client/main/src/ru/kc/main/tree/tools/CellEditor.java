package ru.kc.main.tree.tools;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.TreeCellEditor;

import ru.kc.main.model.NodeIcon;
import ru.kc.main.tree.ui.CellPanel;
import ru.kc.model.Node;
import ru.kc.util.Check;
import ru.kc.util.swing.tree.TreeFacade;

public class CellEditor implements TreeCellEditor {
	
	private TreeFacade treeFacade;
	private CellPanel cellPanel;
	private JTextField text;
	private JLabel label;
	private boolean enabledRequest;
	
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
	}
	
	public void setEnabledRequest() {
		this.enabledRequest = true;
	}

	@Override
	public Object getCellEditorValue() {
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

	@Override
	public boolean stopCellEditing() {
		String name = label.getText();
		boolean empty = Check.isEmpty(name);
		return !empty;
	}

	@Override
	public void cancelCellEditing() {
		enabledRequest = false;
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		Node node = treeFacade.getCurrentObject(Node.class);
		if(node == null) 
			throw new IllegalStateException("expected "+Node.class);
		label.setIcon(NodeIcon.getIcon(node));
		text.setText(node.getName());
		return cellPanel;
	}



}
