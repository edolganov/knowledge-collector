package ru.chapaj.tool.link.collector.ui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

public class LinkText extends JLabel {
	
	private static String suffix = " >>";
	
	private String text;
	private String info;
	
	public LinkText(String s){
		this();
		setText(s);
	}
	
	public LinkText() {
		super();
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setForeground(new Color(100, 100, 100));
		setFocusable(true);
	}
	
	@Override
	public void setText(String text) {
		this.text = text;
		if(info == null){
			super.setText(">> "+text);
		}
		else {
			super.setText(">> "+text+" "+info);
		}
	}
	
	public void setInfo(String s){
		info = s;
		setText(text);
	}
	
	public void addActionListener(final ActionListener actionListener){
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				actionListener.actionPerformed(null);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {	
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
		});
	}
	
	
	

}
