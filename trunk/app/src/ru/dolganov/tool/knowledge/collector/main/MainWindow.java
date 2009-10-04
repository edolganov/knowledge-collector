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
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class MainWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	public JPanel treePanel = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JPanel jPanel2 = null;
	private JScrollPane jScrollPane = null;
	public ExtendTree tree = null;
	public JButton showHideInfoB = null;
	public JButton dirB = null;
	public JButton linkB = null;
	public JButton noteB = null;
	private JPanel jPanel31 = null;
	private JTabbedPane jTabbedPane1 = null;
	private JPanel jPanel41 = null;
	private JScrollPane jScrollPane11 = null;
	public ExtendTree snapTree = null;
	private JPanel jPanel51 = null;
	public JButton jButton2 = null;
	public JButton jButton3 = null;
	public JButton jButton4 = null;
	public JButton jButton5 = null;
	public JPanel infoPanel = null;
	public JTextField path = null;
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
		this.setBounds(new Rectangle(0, 0, 1024, 541));
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
			jContentPane.add(getJPanel31(), null);
			jContentPane.add(getInfoPanel(), null);
			jContentPane.add(getPath(), null);
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
			jPanel.setBounds(new Rectangle(181, 1, 836, 29));
			jPanel.add(getJButton(), null);
			jPanel.add(getJButton1(), null);
			jPanel.add(getJButton2(), null);
			jPanel.add(getDirB(), null);
			jPanel.add(getJButton22(), null);
			jPanel.add(getNoteB(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (treePanel == null) {
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.setRows(1);
			treePanel = new JPanel();
			treePanel.setLayout(gridLayout1);
			treePanel.setBounds(new Rectangle(182, 33, 497, 459));
			treePanel.add(getJPanel2(), null);
		}
		return treePanel;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(766, 6, 61, 19));
			jButton.setText("exit");
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
			jButton1.setBounds(new Rectangle(700, 6, 62, 19));
			jButton1.setText("hide");
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
		if (tree == null) {
			tree = new ExtendTree();
		}
		return tree;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton2() {
		if (showHideInfoB == null) {
			showHideInfoB = new JButton();
			showHideInfoB.setBounds(new Rectangle(123, 6, 32, 19));
			showHideInfoB.setFont(new Font("Dialog", Font.PLAIN, 12));
			showHideInfoB.setText("");
		}
		return showHideInfoB;
	}

	/**
	 * This method initializes dirB	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDirB() {
		if (dirB == null) {
			dirB = new JButton();
			dirB.setBounds(new Rectangle(3, 6, 32, 19));
			dirB.setText("");
			dirB.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return dirB;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton22() {
		if (linkB == null) {
			linkB = new JButton();
			linkB.setBounds(new Rectangle(37, 6, 32, 19));
			linkB.setFont(new Font("Dialog", Font.PLAIN, 12));
			linkB.setText("");
		}
		return linkB;
	}

	/**
	 * This method initializes noteB	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNoteB() {
		if (noteB == null) {
			noteB = new JButton();
			noteB.setBounds(new Rectangle(71, 6, 32, 19));
		}
		return noteB;
	}

	/**
	 * This method initializes jPanel31	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel31() {
		if (jPanel31 == null) {
			jPanel31 = new JPanel();
			jPanel31.setLayout(new BorderLayout());
			jPanel31.setBounds(new Rectangle(3, 3, 176, 489));
			jPanel31.add(getJTabbedPane1(), java.awt.BorderLayout.CENTER);
		}
		return jPanel31;
	}

	/**
	 * This method initializes jTabbedPane1	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane1() {
		if (jTabbedPane1 == null) {
			jTabbedPane1 = new JTabbedPane();
			jTabbedPane1.addTab("snapshot", null, getJPanel41(), null);
			jTabbedPane1.addTab("search", null, getJPanel51(), null);
		}
		return jTabbedPane1;
	}

	/**
	 * This method initializes jPanel41	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel41() {
		if (jPanel41 == null) {
			jPanel41 = new JPanel();
			jPanel41.setLayout(null);
			jPanel41.add(getJScrollPane11(), null);
			jPanel41.add(getJButton23(), null);
			jPanel41.add(getJButton3(), null);
			jPanel41.add(getJButton4(), null);
			jPanel41.add(getJButton5(), null);
		}
		return jPanel41;
	}

	/**
	 * This method initializes jScrollPane11	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane11() {
		if (jScrollPane11 == null) {
			jScrollPane11 = new JScrollPane();
			jScrollPane11.setBounds(new Rectangle(0, 30, 175, 431));
			jScrollPane11.setViewportView(getJTree11());
		}
		return jScrollPane11;
	}

	/**
	 * This method initializes jTree11	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTree11() {
		if (snapTree == null) {
			snapTree = new ExtendTree();
		}
		return snapTree;
	}

	/**
	 * This method initializes jPanel51	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel51() {
		if (jPanel51 == null) {
			jPanel51 = new JPanel();
			jPanel51.setLayout(null);
		}
		return jPanel51;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton23() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setLocation(new Point(4, 5));
			jButton2.setSize(new Dimension(32, 19));
		}
		return jButton2;
	}

	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setBounds(new Rectangle(40, 5, 32, 19));
		}
		return jButton3;
	}

	/**
	 * This method initializes jButton4	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton4() {
		if (jButton4 == null) {
			jButton4 = new JButton();
			jButton4.setBounds(new Rectangle(76, 5, 32, 19));
		}
		return jButton4;
	}

	/**
	 * This method initializes jButton5	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton5() {
		if (jButton5 == null) {
			jButton5 = new JButton();
			jButton5.setBounds(new Rectangle(111, 5, 32, 19));
		}
		return jButton5;
	}

	/**
	 * This method initializes infoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getInfoPanel() {
		if (infoPanel == null) {
			infoPanel = new JPanel();
			infoPanel.setLayout(null);
			infoPanel.setBounds(new Rectangle(682, 33, 332, 459));
		}
		return infoPanel;
	}

	/**
	 * This method initializes path	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getPath() {
		if (path == null) {
			path = new JTextField();
			path.setBounds(new Rectangle(4, 494, 1010, 19));
		}
		return path;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
