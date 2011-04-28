package ru.kc.module.tree.command;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import ru.kc.common.command.Command;
import ru.kc.module.tree.tools.CellEditor;

public class RenameCellRequestCommand extends Command<Void>{
	
	JTree tree;

	public RenameCellRequestCommand(JTree tree) {
		super();
		this.tree = tree;
	}



	@Override
	protected Void invoke() throws Exception {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				TreeCellEditor treeCellEditor = tree.getCellEditor();
				if (treeCellEditor instanceof CellEditor) {
					CellEditor cellEditor = (CellEditor) treeCellEditor;
					cellEditor.setEnabledRequest();
					TreePath selectionPath = tree.getSelectionPath();
					tree.startEditingAtPath(selectionPath);
				}
			}
			
		});
		
		return null;
	}

}
