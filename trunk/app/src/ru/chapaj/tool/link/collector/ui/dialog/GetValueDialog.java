package ru.chapaj.tool.link.collector.ui.dialog;

import java.awt.Dialog;

import javax.swing.JPanel;

import ru.chapaj.tool.link.collector.App;
import ru.chapaj.tool.link.collector.ui.component.PropertyTextPanel;

public class GetValueDialog extends AbstractDialog {
	
	public PropertyTextPanel name;
	
	public GetValueDialog(Dialog frame,String caption) {
		super(frame,caption);

        JPanel root = new JPanel();
        this.uiRoot.add(root);
        
        name = new PropertyTextPanel("name");
        root.add(name);
	}
	
	public static String getValue(String caption){
		App.getDefault().setStopGlobalKeys(true);
		String out = null;
		final GetValueDialog dialog = new GetValueDialog(App.getDefault().getUI(),caption);
		dialog.pack();
		dialog.setVisible(true);
		if(dialog.confirmedAction){
			String text = dialog.name.getText();
			if(text != null && text.length() > 0){
				out = text;
			}
		}
		
		dialog.removeAll();
		dialog.dispose();
		App.getDefault().setStopGlobalKeys(false);
		return out;
	}

}
