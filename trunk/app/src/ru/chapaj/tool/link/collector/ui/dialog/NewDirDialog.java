package ru.chapaj.tool.link.collector.ui.dialog;

import java.awt.Dialog;

import javax.swing.JPanel;

import ru.chapaj.tool.link.collector.App;
import ru.chapaj.tool.link.collector.model.Dir;
import ru.chapaj.tool.link.collector.ui.component.PropertyTextPanel;

public class NewDirDialog extends AbstractDialog {
	
	public PropertyTextPanel name;
	
	public NewDirDialog(Dialog frame) {
		super(frame,"New Dir");

        JPanel root = new JPanel();
        this.uiRoot.add(root);
        
        name = new PropertyTextPanel("name");
        root.add(name);
	}
	
	public static Dir getDir(){
		App.getDefault().setStopGlobalKeys(true);
		Dir out = null;
		final NewDirDialog dialog = new NewDirDialog(App.getDefault().getUI());
		dialog.pack();
		dialog.setVisible(true);
		if(dialog.confirmedAction){
			String text = dialog.name.getText();
			if(text != null && text.length() > 0){
				out = new Dir(text);
			}
		}
		
		dialog.removeAll();
		dialog.dispose();
		App.getDefault().setStopGlobalKeys(false);
		return out;
	}

}
