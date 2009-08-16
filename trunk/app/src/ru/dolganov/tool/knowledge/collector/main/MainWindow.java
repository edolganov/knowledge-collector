package ru.dolganov.tool.knowledge.collector.main;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JTree;
import javax.swing.JTabbedPane;

import ru.chapaj.util.swing.tree.ExtendTree;

import java.awt.FlowLayout;
import java.awt.GridLayout;

public class MainWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JPanel jPanel2 = null;
	private JScrollPane jScrollPane = null;
	public ExtendTree jTree = null;
	private JPanel jPanel3 = null;
	private JTabbedPane jTabbedPane = null;
	private JPanel jPanel4 = null;
	private JPanel jPanel5 = null;
	private JScrollPane jScrollPane1 = null;
	private JTree jTree1 = null;
	public JButton saveB = null;

	/**
	 * @param owner
	 */
	public MainWindow(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(847, 523);
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
			//jContentPane.add(regPanel);
			jContentPane.add(getJPanel(), null);
			//jContentPane.add(treePanel);
			jContentPane.add(getJPanel1(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(new Rectangle(2, 1, 834, 29));
			jPanel.add(getJButton(), null);
			jPanel.add(getJButton1(), null);
			jPanel.add(getJButton2(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.setBounds(new Rectangle(2, 31, 834, 461));
			jPanel1.add(getJPanel2(), null);
			jPanel1.add(getJPanel3(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(784, 6, 42, 19));
			jButton.setText("e");
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(736, 6, 42, 19));
			jButton1.setText("h");
		}
		return jButton1;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			jPanel2 = new JPanel();
			jPanel2.setLayout(gridLayout);
			jPanel2.setBounds(new Rectangle(3, 4, 646, 453));
			jPanel2.add(getJScrollPane(), null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTree());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTree() {
		if (jTree == null) {
			jTree = new ExtendTree();
		}
		return jTree;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BorderLayout());
			jPanel3.setBounds(new Rectangle(652, 4, 179, 453));
			jPanel3.add(getJTabbedPane(), BorderLayout.CENTER);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("snapshot", null, getJPanel4(), null);
			jTabbedPane.addTab("search", null, getJPanel5(), null);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			jPanel4 = new JPanel();
			jPanel4.setLayout(new BorderLayout());
			jPanel4.add(getJScrollPane1(), BorderLayout.CENTER);
		}
		return jPanel4;
	}

	/**
	 * This method initializes jPanel5	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(null);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJTree1());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jTree1	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTree1() {
		if (jTree1 == null) {
			jTree1 = new JTree();
		}
		return jTree1;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton2() {
		if (saveB == null) {
			saveB = new JButton();
			saveB.setBounds(new Rectangle(4, 6, 42, 19));
			saveB.setText("s");
		}
		return saveB;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
