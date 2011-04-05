package ru.kc.module.snapshots.tools;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import ru.kc.common.controller.Controller;
import ru.kc.module.snapshots.ui.SnapshotsPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.tree.MenuController;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SnapshotsPanel.class)
public class TreeMenuController extends Controller<SnapshotsPanel>{

	private TreeFacade treeFacade;
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		
		final CellEditor cellEditor = new CellEditor(ui.tree);
		cellEditor.addCustomListener(new CellEditorListener() {
			
			@Override
			public void editingStopped(ChangeEvent e) {
				System.out.println("update!!!");
			}
			
			@Override
			public void editingCanceled(ChangeEvent e) {}
		});
		ui.tree.setCellEditor(cellEditor);
		ui.tree.setRowHeight(0);
		ui.tree.setEditable(true);
		treeFacade.setSingleSelection();
		treeFacade.setPopupMenu(new TreeMenu(ui.tree, appContext), new MenuController() {
			
			@Override
			public boolean canShow() {
				return ! cellEditor.isEnabled();
			}
		});
	}
	

}
