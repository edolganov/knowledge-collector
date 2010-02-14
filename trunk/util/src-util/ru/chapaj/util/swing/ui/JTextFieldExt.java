package ru.chapaj.util.swing.ui;

import javax.swing.JTextField;
import javax.swing.text.Document;

import ru.chapaj.util.swing.menu.MenuUtil;
import ru.chapaj.util.swing.ui.text.TextComponentUtil;

public class JTextFieldExt extends JTextField {

	public JTextFieldExt() {
		super();
		init();
	}


	public JTextFieldExt(Document doc, String text, int columns) {
		super(doc, text, columns);
		init();
	}


	public JTextFieldExt(int columns) {
		super(columns);
		init();
	}


	public JTextFieldExt(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		init();
	}

	public JTextFieldExt(String text) {
		super(text);
		init();
	}

	private void init() {
		MenuUtil.initCopyPastMenu(this);
		TextComponentUtil.undoRedoInit(this);
	}
	
	

}


