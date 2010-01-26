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
import javax.swing.JList;
import javax.swing.BoxLayout;
import java.awt.CardLayout;

public class MainWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	public JPanel treePanel = null;
	public JButton jButton = null;
	public JButton jButton1 = null;
	private JPanel jPanel2 = null;
	private JScrollPane jScrollPane = null;
	public ExtendTree tree = null;
	public JButton showHideInfoB = null;
	public JButton dirB = null;
	public JButton linkB = null;
	public JButton noteB = null;
	private JTabbedPane jTabbedPane1 = null;
	private JPanel jPanel41 = null;
	private JScrollPane jScrollPane11 = null;
	public ExtendTree snapTree = null;
	private JPanel jPanel51 = null;
	public JButton createSnapDirB = null;
	public JButton createSnapB = null;
	public JButton jButton4 = null;
	public JButton jButton5 = null;
	public JPanel infoPanel = null;
	public JTextField path = null;
	public JTextField searchF = null;
	private JScrollPane jScrollPane1 = null;
	public JList searchList = null;
	private JPanel downPanel = null;
	private JPanel upPanel = null;
	private JPanel leftPanel = null;
	private JPanel rigthPanel = null;
	private JPanel buttons = null;
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
		this.setMinimumSize(new Dimension(1024, 541));
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(2);
			borderLayout.setVgap(2);
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout);
			jContentPane.add(getDownPanel(), BorderLayout.SOUTH);
			jContentPane.add(getUpPanel(), BorderLayout.CENTER);
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
			jPanel.setPreferredSize(new Dimension(1, 29));
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
	 * This method initializes jTabbedPane1	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane1() {
		if (jTabbedPane1 == null) {
			jTabbedPane1 = new JTabbedPane();
			jTabbedPane1.setPreferredSize(new Dimension(176, 48));
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
			jPanel41.setLayout(new BorderLayout());
			jPanel41.add(getJScrollPane11(), BorderLayout.CENTER);
			jPanel41.add(getButtons(), BorderLayout.NORTH);
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
			jPanel51.add(getSearchF(), null);
			jPanel51.add(getJScrollPane1(), null);
		}
		return jPanel51;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton23() {
		if (createSnapDirB == null) {
			createSnapDirB = new JButton();
			createSnapDirB.setLocation(new Point(9, 3));
			createSnapDirB.setSize(new Dimension(32, 19));
		}
		return createSnapDirB;
	}

	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton3() {
		if (createSnapB == null) {
			createSnapB = new JButton();
			createSnapB.setLocation(new Point(45, 3));
			createSnapB.setSize(new Dimension(32, 19));
		}
		return createSnapB;
	}

	/**
	 * This method initializes jButton4	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton4() {
		if (jButton4 == null) {
			jButton4 = new JButton();
			jButton4.setLocation(new Point(87, 3));
			jButton4.setSize(new Dimension(32, 19));
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
			jButton5.setLocation(new Point(121, 3));
			jButton5.setSize(new Dimension(32, 19));
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
			infoPanel.setPreferredSize(new Dimension(332, 100));
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
			path.setPreferredSize(new Dimension(4, 19));
		}
		return path;
	}

	/**
	 * This method initializes searchF	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearchF() {
		if (searchF == null) {
			searchF = new JTextField();
			searchF.setBounds(new Rectangle(4, 2, 165, 19));
		}
		return searchF;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(new Rectangle(4, 25, 165, 432));
			jScrollPane1.setViewportView(getSearchList());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes searchList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getSearchList() {
		if (searchList == null) {
			searchList = new JList();
		}
		return searchList;
	}

	/**
	 * This method initializes downPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDownPanel() {
		if (downPanel == null) {
			downPanel = new JPanel();
			downPanel.setLayout(new BorderLayout());
			downPanel.setName("downPanel");
			downPanel.add(getPath(), BorderLayout.NORTH);
		}
		return downPanel;
	}

	/**
	 * This method initializes upPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getUpPanel() {
		if (upPanel == null) {
			BorderLayout borderLayout1 = new BorderLayout();
			borderLayout1.setHgap(4);
			upPanel = new JPanel();
			upPanel.setLayout(borderLayout1);
			upPanel.setName("upPanel");
			upPanel.add(getLeftPanel(), BorderLayout.WEST);
			upPanel.add(getRigthPanel(), BorderLayout.CENTER);
		}
		return upPanel;
	}

	/**
	 * This method initializes leftPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLeftPanel() {
		if (leftPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.weightx = 1.0;
			leftPanel = new JPanel();
			leftPanel.setLayout(new GridBagLayout());
			leftPanel.setPreferredSize(new Dimension(180, 48));
			leftPanel.add(getJTabbedPane1(), gridBagConstraints4);
		}
		return leftPanel;
	}

	/**
	 * This method initializes rigthPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRigthPanel() {
		if (rigthPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = -1;
			gridBagConstraints.gridy = -1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = -1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.gridy = -1;
			rigthPanel = new JPanel();
			rigthPanel.setLayout(new BorderLayout());
			rigthPanel.add(getInfoPanel(), BorderLayout.EAST);
			rigthPanel.add(getJPanel1(), BorderLayout.CENTER);
			rigthPanel.add(getJPanel(), BorderLayout.NORTH);
		}
		return rigthPanel;
	}

	/**
	 * This method initializes buttons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtons() {
		if (buttons == null) {
			buttons = new JPanel();
			buttons.setLayout(null);
			buttons.setPreferredSize(new Dimension(136, 25));
			buttons.add(getJButton23(), null);
			buttons.add(getJButton3(), null);
			buttons.add(getJButton4(), null);
			buttons.add(getJButton5(), null);
		}
		return buttons;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
