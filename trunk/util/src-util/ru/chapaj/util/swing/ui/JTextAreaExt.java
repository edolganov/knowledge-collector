package ru.chapaj.util.swing.ui;

import javax.swing.JTextArea;
import javax.swing.text.Document;

import ru.chapaj.util.swing.menu.MenuUtil;

public class JTextAreaExt extends JTextArea {

	public JTextAreaExt() {
		super();
		init();
	}

	public JTextAreaExt(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		init();
	}

	public JTextAreaExt(Document doc) {
		super(doc);
		init();
	}

	public JTextAreaExt(int rows, int columns) {
		super(rows, columns);
		init();
	}

	public JTextAreaExt(String text, int rows, int columns) {
		super(text, rows, columns);
		init();
	}

	public JTextAreaExt(String text) {
		super(text);
		init();
	}

	private void init() {
		MenuUtil.initCopyPastMenu(this);
	}
	
}
