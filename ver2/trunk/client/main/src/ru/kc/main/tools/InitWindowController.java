package ru.kc.main.tools;

import java.awt.Dimension;
import java.awt.Toolkit;

import ru.kc.common.controller.Controller;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.ui.tabbedform.MainForm;
import ru.kc.util.swing.icon.IconUtil;

@Mapping(MainForm.class)
public class InitWindowController extends Controller<MainForm> {

	@Override
	protected void init() {
		hideUnusedElements();
		titleAndIcon();
		size();
        position();
	}

	private void titleAndIcon() {
		ui.setTitle("Knowledge Collector [2.0-alpha]");
		ui.setIconImage(IconUtil.get("/ru/kc/common/img/app.png").getImage());
	}

	private void hideUnusedElements() {
		ui.menu.setVisible(false);
		ui.root.header.setVisible(false);
		ui.root.footer.setVisible(false);
	}

	private void position() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = ui.getSize();
        int windowX = Math.max(0, (screenSize.width - windowSize.width) / 2);
        int windowY = Math.max(0, (screenSize.height - windowSize.height) / 2);
        ui.setLocation(windowX, windowY); 
        //ui.setLocationByPlatform(true);
	}

	private void size() {
		int width = 1024;
		int height = 540;
		ui.setMinimumSize(new Dimension(560, 390));
		ui.setSize(width, height);
	}

}
