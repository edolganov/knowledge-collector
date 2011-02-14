package ru.kc.module.properties;

import ru.kc.module.properties.ui.PropsPanel;
import ru.kc.platform.annotations.GlobalMapping;
import ru.kc.platform.module.Module;

@GlobalMapping("props")
public class PropsModule extends Module<PropsPanel> {

	@Override
	protected PropsPanel createUI() {
		return new PropsPanel();
	}

}
