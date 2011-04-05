package ru.kc.module.snapshots.tools;

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
		
		treeFacade.setPopupMenu(new TreeMenu(ui.tree, appContext), new MenuController() {
			
			@Override
			public boolean canShow() {
				return true;//! cellEditor.isEnabled();
			}
		});
	}
	

}
