package ru.kc.main.tree.tools;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.TreeCellEditor;

import ru.kc.main.tree.ui.CellPanel;

public class CellEditor implements TreeCellEditor {
	
	CellPanel cellPanel = new CellPanel();
	
	public CellEditor() {
		cellPanel.setOpaque(false);
	}

	@Override
	public Object getCellEditorValue() {
		System.out.println("getCellEditorValue");
		return "test";
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		System.out.println("isCellEditable");
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		System.out.println("shouldSelectCell");
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		System.out.println("stopCellEditing");
		return false;
	}

	@Override
	public void cancelCellEditing() {
		System.out.println("cancelCellEditing");
		
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		System.out.println("getTreeCellEditorComponent");
		return cellPanel;
	}

}
