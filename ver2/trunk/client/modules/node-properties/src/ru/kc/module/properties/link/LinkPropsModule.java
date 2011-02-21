package ru.kc.module.properties.link;

import ru.kc.model.Link;
import ru.kc.module.properties.PropsUpdater;
import ru.kc.module.properties.ui.LinkProps;
import ru.kc.platform.module.Module;


@SuppressWarnings("serial")
public class LinkPropsModule extends Module<LinkProps> implements PropsUpdater{

	@Override
	protected LinkProps createUI() {
		return new LinkProps();
	}

	public void setNode(Link node) {
		getController(LinkPropsController.class).setNode(node);
	}

	@Override
	public void enableUpdateMode() {
		getController(LinkPropsController.class).enableUpdateMode();
	}

	@Override
	public void disableUpdateMode() {
		getController(LinkPropsController.class).disableUpdateMode();
	}


}
