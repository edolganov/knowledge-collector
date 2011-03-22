package ru.kc.common.tree;

import java.util.List;

public interface TreeService {
	
	TreeNodeFacade getRoot();
	
	List<TreeNodeFacade> getChildren(TreeNodeFacade node);
	
	

}
