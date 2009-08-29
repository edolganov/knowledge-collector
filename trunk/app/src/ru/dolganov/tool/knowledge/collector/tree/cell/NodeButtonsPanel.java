package ru.dolganov.tool.knowledge.collector.tree.cell;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.Rectangle;

public class NodeButtonsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public JButton dirB = null;
	public JButton noteB = null;
	public JButton linkB = null;

	/**
	 * This is the default constructor
	 */
	public NodeButtonsPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(103, 18);
		this.setLayout(null);
		this.setPreferredSize(new Dimension(126, 18));
		this.add(getDirB(), null);
		this.add(getNoteB(), null);
		this.add(getLinkB(), null);
	}

	/**
	 * This method initializes dirB	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDirB() {
		if (dirB == null) {
			dirB = new JButton();
			dirB.setBounds(new Rectangle(4, 1, 31, 16));
		}
		return dirB;
	}

	/**
	 * This method initializes noteB	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNoteB() {
		if (noteB == null) {
			noteB = new JButton();
			noteB.setBounds(new Rectangle(37, 1, 31, 16));
		}
		return noteB;
	}

	/**
	 * This method initializes linkB	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLinkB() {
		if (linkB == null) {
			linkB = new JButton();
			linkB.setBounds(new Rectangle(69, 1, 31, 16));
		}
		return linkB;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
