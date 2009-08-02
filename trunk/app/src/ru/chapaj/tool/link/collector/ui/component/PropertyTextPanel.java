package ru.chapaj.tool.link.collector.ui.component;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Font;

public class PropertyTextPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public JLabel label = null;
	public JTextField textField = null;

	/**
	 * This is the default constructor
	 */
	public PropertyTextPanel() {
		super();
		initialize();
	}
	
	public PropertyTextPanel(String labelText) {
		this();
		label.setText(labelText);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.gridwidth = 10;
		gridBagConstraints1.ipadx = 13;
		gridBagConstraints1.anchor = GridBagConstraints.CENTER;
		gridBagConstraints1.gridx = 10;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 10;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.ipadx = 120;
		gridBagConstraints.gridy = 0;
		label = new JLabel();
		label.setText("label");
		label.setFont(new Font("Dialog", Font.BOLD, 10));
		label.setPreferredSize(new Dimension(60, 16));
		this.setSize(290, 35);
		this.setLayout(new GridBagLayout());
		this.add(label, gridBagConstraints);
		this.add(getTextField(), gridBagConstraints1);
	}

	/**
	 * This method initializes textField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField(20);
		}
		return textField;
	}
	
	
	public String getText(){
		return textField.getText();
	}
	
	public void setText(String s){
		if(s == null) s = "";
		textField.setText(new String(s));
		textField.setCaretPosition(0);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
