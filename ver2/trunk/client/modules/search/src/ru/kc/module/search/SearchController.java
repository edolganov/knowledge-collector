package ru.kc.module.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.common.search.event.SearchRequest;
import ru.kc.model.Node;
import ru.kc.module.search.tools.ByGroupSorter;
import ru.kc.module.search.tools.CellRender;
import ru.kc.module.search.tools.Group;
import ru.kc.module.search.tools.GroupComparator;
import ru.kc.module.search.ui.SearchPanel;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.data.Answer;
import ru.kc.util.Check;
import ru.kc.util.swing.component.ComponentUtil;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(SearchPanel.class)
public class SearchController extends Controller<SearchPanel>{

	TreeFacade treeFacade;
	GroupComparator byGroupComparator = new GroupComparator();
	
	@SuppressWarnings("serial")
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
		cleanTree();
		ui.tree.setRootVisible(false);
		ui.tree.setCellRenderer(new CellRender());
		
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
		
		ui.settings.setEnabled(false);
	}

	protected void search() {
		String searchQuery = ui.text.getText();
		if(Check.isEmpty(searchQuery)){
			cleanTree();
		} else {
			List<Node> result = searchRequest(searchQuery);
			if(!result.isEmpty()){
				buildTree(result);
			} else {
				emptyResultTree();
			}
		}
	}
	
	private void buildTree(List<Node> list) {
		cleanTree();
		ByGroupSorter byGroupSorter = new ByGroupSorter(list);
		for(Group group : byGroupSorter){
			addGroupToTree(group);
		}
		DefaultMutableTreeNode root = treeFacade.getRoot();
		for(int i = 0; i < root.getChildCount(); ++i){
			treeFacade.expand((DefaultMutableTreeNode)root.getChildAt(i));
		}
		
	}

	private void addGroupToTree(Group group) {
		DefaultMutableTreeNode root = treeFacade.getRoot();
		DefaultMutableTreeNode groupNode = treeFacade.addChild(root, group);
		for(Node node : group.nodes){
			treeFacade.addChild(groupNode, node);
		}
	}

	private void emptyResultTree() {
		cleanTree();
		treeFacade.addChild(treeFacade.getRoot(), "empty result");
		
	}



	private void cleanTree() {
		ui.tree.setModel(TreeFacade.createModelByUserObject(""));
	}
	
	private List<Node> searchRequest(String searchQuery) {
		Answer<List<Node>> answer = invokeSafe(new SearchRequest(searchQuery));
		if(answer.isValid()){
			List<Node> result = answer.result;
			return result;
		} else {
			return new ArrayList<Node>();
		}
	}

}
