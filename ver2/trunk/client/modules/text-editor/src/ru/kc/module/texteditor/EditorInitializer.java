package ru.kc.module.texteditor;

import jsyntaxpane.DefaultSyntaxKit;
import ru.kc.platform.init.BeforeStartInitializer;

public class EditorInitializer implements BeforeStartInitializer {

	@Override
	public void doAction() {
		DefaultSyntaxKit.initKit(); 
	}

}
