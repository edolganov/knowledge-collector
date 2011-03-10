package ru.kc.module.properties.filelink;

import ru.kc.model.FileLink;
import ru.kc.module.properties.PropsUpdater;
import ru.kc.module.properties.ui.FileLinkProps;
import ru.kc.platform.module.Module;


@SuppressWarnings("serial")
public class FileLinkPropsModule extends Module<FileLinkProps> implements PropsUpdater{

	@Override
	protected FileLinkProps createUI() {
		return new FileLinkProps();
	}

	public void setNode(FileLink node) {
		getController(FileLinkPropsController.class).setNode(node);
	}

	@Override
	public void enableUpdateMode() {
		getController(FileLinkPropsController.class).enableUpdateMode();
	}

	@Override
	public void disableUpdateMode() {
		getController(FileLinkPropsController.class).disableUpdateMode();
	}


}
