package ru.kc.module.properties;

import java.awt.BorderLayout;

import ru.kc.common.controller.Controller;
import ru.kc.module.properties.ui.PropsPanel;
import ru.kc.platform.annotations.Mapping;

@Mapping(PropsPanel.class)
public class PropsController extends Controller<PropsPanel> {

	@Override
	protected void init() {
		ui.setLayout(new BorderLayout());
	}

}
