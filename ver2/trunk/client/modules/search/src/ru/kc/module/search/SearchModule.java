package ru.kc.module.search;

import ru.kc.module.search.ui.SearchPanel;
import ru.kc.platform.annotations.GlobalMapping;
import ru.kc.platform.module.Module;

@SuppressWarnings("serial")
@GlobalMapping("search")
public class SearchModule extends Module<SearchPanel>{

	@Override
	protected SearchPanel createUI() {
		return new SearchPanel();
	}

}
