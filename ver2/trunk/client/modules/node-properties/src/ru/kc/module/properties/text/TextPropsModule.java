package ru.kc.module.properties.text;

import ru.kc.model.Text;
import ru.kc.module.properties.PropsUpdater;
import ru.kc.module.properties.ui.TextProps;
import ru.kc.platform.module.Module;


@SuppressWarnings("serial")
public class TextPropsModule extends Module<TextProps> implements PropsUpdater{

	@Override
	protected TextProps createUI() {
		return new TextProps();
	}

	public void setNode(Text node) {
		getController(TextPropsController.class).setNode(node);
	}

	@Override
	public void enableUpdateMode() {
		getController(TextPropsController.class).enableUpdateMode();
	}

	@Override
	public void disableUpdateMode() {
		getController(TextPropsController.class).disableUpdateMode();
	}


}
