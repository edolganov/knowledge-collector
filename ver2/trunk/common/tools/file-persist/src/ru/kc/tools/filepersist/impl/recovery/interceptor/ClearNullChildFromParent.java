package ru.kc.tools.filepersist.impl.recovery.interceptor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.ActionInterceptor;
import ru.kc.tools.filepersist.persist.transaction.actions.GetChildren;

public class ClearNullChildFromParent implements ActionInterceptor {
	
	private static Log log = LogFactory.getLog(ClearNullChildFromParent.class);

	@Override
	public Class<?>[] types() {
		return new Class[]{GetChildren.class};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterInvoke(Object actionObject, Object result) {
		GetChildren action = (GetChildren) actionObject;
		List<NodeBean> list = (List<NodeBean>)result;
		
		boolean hasError = false;
		boolean fixed = false;
		for(int i = list.size()-1; i > -1; i--){
			NodeBean nodeBean = list.get(i);
			if(nodeBean == null){
				hasError = true;
				List<String> childrenIds = action.parent.getChildrenIds();
				String childId = "[unknow]";
				if(i < childrenIds.size()){
					childId = childrenIds.remove(i);
					fixed = true;
				}
				log.error("Parent "+action.parent+" contains null reference by id "+childId);
				list.remove(i);
			}
		}
		
		if(hasError){
			if(fixed){
				log.info("parent fixed");
			} else {
				log.info("can't fix parent");
			}
		}

	}

}
