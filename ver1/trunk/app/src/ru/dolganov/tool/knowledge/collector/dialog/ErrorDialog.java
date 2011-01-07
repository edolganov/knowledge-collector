package ru.dolganov.tool.knowledge.collector.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ru.chapaj.util.swing.dialog.ExtendDialog;

public class ErrorDialog extends ExtendDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	public JButton ok = null;
	public JLabel label2 = null;

	/**
	 * @param owner
	 */
	public ErrorDialog() {
		super((JDialog)null);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 104);
		this.setSize(new Dimension(296, 104));
		this.setTitle("Sorry!");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			label2 = new JLabel();
			label2.setBounds(new Rectangle(11, 12, 274, 27));
			label2.setFont(new Font("Dialog", Font.PLAIN, 12));
			label2.setHorizontalTextPosition(SwingConstants.CENTER);
			label2.setHorizontalAlignment(SwingConstants.CENTER);
			label2.setText("text");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getOk(), null);
			jContentPane.add(label2, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes ok	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOk() {
		if (ok == null) {
			ok = new JButton();
			ok.setBounds(new Rectangle(116, 50, 55, 19));
			ok.setText("Ok");
		}
		return ok;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
