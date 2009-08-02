package ru.chapaj.tool.link.collector.ui.dialog;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ru.chapaj.tool.link.collector.App;
import ru.chapaj.tool.link.collector.model.Link;
import ru.chapaj.tool.link.collector.ui.component.PropertyTextPanel;

public class NewLinkDialog extends AbstractDialog {
	
	public PropertyTextPanel name,url;
	
	public NewLinkDialog(Dialog frame) {
		super(frame,"New Link");
        
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        this.uiRoot.add(root);
        
        name = new PropertyTextPanel("name");
        url = new PropertyTextPanel("url");
        root.add(url);
        root.add(name);
	}
	
	public static Link getLink(){
		App.getDefault().setStopGlobalKeys(true);
		Link out = null;
		final NewLinkDialog dialog = new NewLinkDialog(App.getDefault().getUI());
		dialog.pack();
		dialog.setVisible(true);
		if(dialog.confirmedAction){
			String url = dialog.url.getText();
			if(url != null && url.length() > 0){
				out = new Link();
				out.setUrl(url);
				String name = dialog.name.getText();
				if(name == null || name.length() == 0){
					name = new String(url);
				}
				out.setName(name);
			}
		}
		
		dialog.removeAll();
		dialog.dispose();
		App.getDefault().setStopGlobalKeys(false);
		return out;
	}

}
