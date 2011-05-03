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
//		setFont(textArea, new Font("Arial", Font.PLAIN, 16));
		return textArea;
	}
	

	   public static void setFont(RSyntaxTextArea textArea, Font font) {
         SyntaxScheme ss = textArea.getSyntaxScheme();
         ss = (SyntaxScheme)ss.clone();
         for (int i=0; i<ss.styles.length; i++) {
            if (ss.styles[i]!=null) {
               ss.styles[i].font = font;
		    }
		 }
		 textArea.setSyntaxScheme(ss);
		 textArea.setFont(font);
	   }

}
