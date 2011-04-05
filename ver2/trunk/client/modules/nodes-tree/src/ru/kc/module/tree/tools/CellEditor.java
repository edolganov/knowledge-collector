package ru.kc.module.tree.tools;

import javax.swing.JTree;

import ru.kc.common.node.NodeIcon;
import ru.kc.model.Node;
import ru.kc.util.Check;
import ru.kc.util.swing.tree.AbstractCellEditor;

public class CellEditor extends AbstractCellEditor {
	
	public CellEditor(JTree tree) {
		super(tree);
	}
	
	@Override
	protected boolean isStopCellEditing() {
		String name = text.getText();
		
		Node node = treeFacade.getCurrentObject(Node.class);
		if(node != null && node.getName().equals(name)){
			return false;
		}
			
		boolean empty = Check.isEmpty(name);
		if(empty) return false;
		
		return true;
	}
	
	@Override
	protected void fillCellEditorPanel() {
		Node node = treeFacade.getCurrentObject(Node.class);
		if(node == null) 
			throw new IllegalStateException("expected "+Node.class);
		label.setIcon(NodeIcon.getIcon(node));
		text.setText(node.getName());
	}





}
