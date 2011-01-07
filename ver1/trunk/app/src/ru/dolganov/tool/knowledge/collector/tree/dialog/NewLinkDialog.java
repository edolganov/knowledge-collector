package ru.dolganov.tool.knowledge.collector.tree.dialog;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;

import ru.chapaj.tool.link.collector.ui.component.PropertyTextPanel;
import ru.chapaj.util.swing.dialog.ExtendDialog;

import java.awt.Rectangle;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.Dimension;

public class NewLinkDialog extends ExtendDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	public PropertyTextPanel namePanel = null;
	public JButton ok = null;
	public PropertyTextPanel urlPanel = null;

	/**
	 * @param owner
	 */
	public NewLinkDialog() {
		super((JDialog)null);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 135);
		this.setMaximumSize(new Dimension(300, 135));
		this.setMinimumSize(new Dimension(300, 135));
		this.setSize(new Dimension(296, 104));
		this.setTitle("New Link");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getNamePanel(), null);
			jContentPane.add(getOk(), null);
			jContentPane.add(getLinkLabel(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes namePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNamePanel() {
		if (namePanel == null) {
			namePanel = new PropertyTextPanel();
			namePanel.setBounds(new Rectangle(2, 3, 287, 35));
			namePanel.label.setText("name");
		}
		return namePanel;
	}

	/**
	 * This method initializes ok	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOk() {
		if (ok == null) {
			ok = new JButton();
			ok.setBounds(new Rectangle(116, 84, 55, 19));
			ok.setText("Ok");
		}
		return ok;
	}

	/**
	 * This method initializes linkLabel	
	 * 	
	 * @return ru.chapaj.tool.link.collector.ui.component.PropertyTextPanel	
	 */
	private PropertyTextPanel getLinkLabel() {
		if (urlPanel == null) {
			urlPanel = new PropertyTextPanel();
			urlPanel.setBounds(new Rectangle(2, 41, 287, 35));
			urlPanel.label.setText("url");
		}
		return urlPanel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
