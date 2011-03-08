package ru.kc.module.tree.tools;

import java.util.Collection;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import ru.kc.common.controller.Controller;
import ru.kc.common.tree.event.NodeSelected;
import ru.kc.model.Node;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class TreeSelectController extends Controller<Tree> {

	TreeFacade treeFacade;
	
	@Override
	protected void init() {
		
		treeFacade = new TreeFacade(ui.tree);
		
		ui.tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Node node = treeFacade.getCurrentObject(Node.class);
				if(node != null){
					fireEventInEDT(new NodeSelected(node));
				} else {
					fireEventInEDT(new NodeSelected(null));
				}
			}
			
		});
		

	}
	
	@Override
	protected void onNodeUpdated(Node old, Node updatedNode, Collection<UpdateRequest> updates) {
		Node node = treeFacade.getCurrentObject(Node.class);
		if(node == null){
			fireEventInEDT(new NodeSelected(null));
		}
	}

}
