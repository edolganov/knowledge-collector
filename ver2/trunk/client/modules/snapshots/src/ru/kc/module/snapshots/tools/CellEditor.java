package ru.kc.module.snapshots.tools;

import javax.swing.JTree;

import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.util.Check;
import ru.kc.util.swing.tree.AbstractCellEditor;

public class CellEditor extends AbstractCellEditor {
	
	public CellEditor(JTree tree) {
		super(tree);
	}
	
	@Override
	protected boolean isStopCellEditing() {
		String name = text.getText();
		
		boolean empty = Check.isEmpty(name);
		if(empty) return false;
		
		String oldName = getOldName();
		if(oldName != null && oldName.equals(name)){
			return false;
		}
		return true;
	}
	
	private String getOldName() {
		Object ob = treeFacade.getCurrentObject();
		if(ob instanceof SnapshotDir){
			return ((SnapshotDir) ob).getName();
		}
		if(ob instanceof Snapshot){
			return ((Snapshot) ob).getName();
		}
		return null;
	}





}
