package ru.kc.module.texteditor.ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rtextarea.RTextScrollPane;

public class TextEditor extends JPanel {
	
	public RTextScrollPane scrollPane;
	public RSyntaxTextArea editor;
	
	public TextEditor() {
		setLayout(new BorderLayout());
		editor = createTextArea();
		scrollPane = new RTextScrollPane(editor, false);
		add(scrollPane);
	}

	private RSyntaxTextArea createTextArea() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrences(true);
//		textArea.setCaretPosition(0);
//		textArea.addHyperlinkListener(this);
//		textArea.requestFocusInWindow();
//		textArea.setTextAntiAliasHint("VALUE_TEXT_ANTIALIAS_ON");
		return textArea;
	}

}
