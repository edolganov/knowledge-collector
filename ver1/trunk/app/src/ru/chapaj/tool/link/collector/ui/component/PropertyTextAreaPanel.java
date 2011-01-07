package ru.chapaj.tool.link.collector.ui.component;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ru.chapaj.util.swing.ui.JTextAreaExt;

public class PropertyTextAreaPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public JLabel label = null;
	private JScrollPane jScrollPane = null;
	public JTextArea textArea = null;
	/**
	 * This is the default constructor
	 */
	public PropertyTextAreaPanel() {
		super();
		initialize();
	}
	
	public PropertyTextAreaPanel(String labelText) {
		this();
		if(labelText != null) label.setText(labelText);
		else label.setVisible(false);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.gridx = 10;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 10;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.ipadx = 120;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 0;
		label = new JLabel();
		label.setText("label");
		label.setFont(new Font("Dialog", Font.BOLD, 10));
		label.setPreferredSize(new Dimension(60, 16));
		this.setSize(290, 99);
		this.setLayout(new GridBagLayout());
		this.add(label, gridBagConstraints);
		this.add(getJScrollPane(), gridBagConstraints1);
	}

	public String getText(){
		return textArea.getText();
	}
	
	public void setText(String s){
		if(s == null) s = "";
		textArea.setText(new String(s));
		textArea.setCaretPosition(0);
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTextArea());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes textArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTextArea() {
		if (textArea == null) {
			textArea = new JTextAreaExt();
			textArea.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return textArea;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
