package ru.dolganov.tool.knowledge.collector.tree.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.knowledge.Dir;
import model.knowledge.Link;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.Note;
import model.knowledge.TextData;
import ru.chapaj.util.Check;
import ru.chapaj.util.swing.listener.KeyEnterAdapter;
import ru.dolganov.tool.knowledge.collector.App;
import ru.dolganov.tool.knowledge.collector.model.LinkOps;

public class DialogOps {
	
	public static Dir newDir(){
		Dir out = null;
		
		final NewDirDialog newDir = new NewDirDialog();
		newDir.init(App.getDefault().getUI());
		
		newDir.ok.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				newDir.setConfirmedActionAndExit(true);
			}
		});
		
		newDir.namePanel.textField.addKeyListener(new KeyEnterAdapter(){

			@Override
			public void onEnter() {
				newDir.setConfirmedActionAndExit(true);
			}
			
		});
		
		newDir.setVisible(true);
		
		if(newDir.isConfirmedAction()){
			String name = newDir.namePanel.textField.getText();
			if(!Check.isEmpty(name)){
				out = new Dir();
				out.setName(name);
			}
		}
		newDir.remove();
		return out;
	}
	
	public static Link newLink(){
		Link out = null;
		
		final NewLinkDialog newLink = new NewLinkDialog();
		newLink.init(App.getDefault().getUI());
		
		newLink.ok.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				newLink.setConfirmedActionAndExit(true);
			}
		});
		
		KeyEnterAdapter keyEnterAdapter = new KeyEnterAdapter(){

			@Override
			public void onEnter() {
				newLink.setConfirmedActionAndExit(true);
			}
			
		};
		newLink.namePanel.textField.addKeyListener(keyEnterAdapter);
		newLink.urlPanel.textField.addKeyListener(keyEnterAdapter);
		
		newLink.setVisible(true);
		
		if(newLink.isConfirmedAction()){
			String name = newLink.namePanel.textField.getText();
			String url = newLink.urlPanel.textField.getText();
			if(!Check.isEmpty(name) || !Check.isEmpty(url)) {
				if(!Check.isEmpty(url)){
					if(LinkOps.isInetLink(url)){
						out = new NetworkLink();
					}
					else if(LinkOps.isLocalLink(url)){
						out = new LocalLink();
					}
					else {
						out = new NetworkLink();
					}
					out.setUrl(url);
				}else{
					out = new NetworkLink();
				}
				
				if(!Check.isEmpty(name)){
					out.setName(name);
				}
				else {
					out.setName(url);
				}
			}
		}
		newLink.remove();
		
		
		return out;
	}
	
	public static TextData newText(){
		TextData out = null;
		
		final NewDirDialog newText = new NewDirDialog();
		newText.setTitle("New Text");
		newText.init(App.getDefault().getUI());
		
		newText.ok.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				newText.setConfirmedActionAndExit(true);
			}
		});
		
		newText.namePanel.textField.addKeyListener(new KeyEnterAdapter(){

			@Override
			public void onEnter() {
				newText.setConfirmedActionAndExit(true);
			}
			
		});
		
		newText.setVisible(true);
		
		if(newText.isConfirmedAction()){
			String name = newText.namePanel.textField.getText();
			if(!Check.isEmpty(name)){
				out = new Note();
				out.setName(name);
			}
		}
		newText.remove();
		return out;
	}
	

}
