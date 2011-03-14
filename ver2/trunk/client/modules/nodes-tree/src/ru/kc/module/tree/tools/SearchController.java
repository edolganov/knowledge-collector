package ru.kc.module.tree.tools;

import java.util.ArrayList;

import ru.kc.common.controller.Controller;
import ru.kc.common.search.event.SearchRequest;
import ru.kc.model.Node;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class SearchController extends Controller<Tree> {
	
	
	TreeFacade facade;
	
	@Override
	protected void init() {
		facade = new TreeFacade(ui.tree);

	}
	
	
	@EventListener
	public void onSearchRequest(SearchRequest request){
		System.out.println("!!!");
		request.setResponse(new ArrayList<Node>());
	}


}
