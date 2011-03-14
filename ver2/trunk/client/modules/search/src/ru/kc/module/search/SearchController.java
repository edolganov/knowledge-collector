package ru.kc.module.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.tree.DefaultTreeModel;

import ru.kc.common.controller.Controller;
import ru.kc.module.search.ui.SearchPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.component.ComponentUtil;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SearchPanel.class)
public class SearchController extends Controller<SearchPanel>{

	TreeFacade treeFacade;
	
	@SuppressWarnings("serial")
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		DefaultTreeModel model = TreeFacade.createModelByUserObject("root");
		ui.tree.setModel(model);
		ui.tree.setRootVisible(false);
		
		ComponentUtil.addAction(ui.text, "ENTER", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		
		ui.search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
	}

	protected void search() {
		
	}

}
