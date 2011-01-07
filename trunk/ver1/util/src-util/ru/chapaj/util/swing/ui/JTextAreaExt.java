package ru.chapaj.util.swing.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import ru.chapaj.util.swing.menu.MenuUtil;
import ru.chapaj.util.swing.ui.text.TextComponentUtil;

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
		TextComponentUtil.undoRedoInit(this);
	}
	
}
