package ru.chapaj.tool.link.collector.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import ru.chapaj.tool.link.collector.ui.component.LinkText;
import ru.chapaj.tool.link.collector.ui.component.PropertyTextAreaPanel;
import ru.chapaj.tool.link.collector.ui.component.PropertyTextPanel;
import ru.chapaj.util.swing.tree.DNDTree;
import ru.chapaj.util.swing.tree.ExtendTree;

import javax.swing.JList;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JTextField;

public class AppFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	
	public JButton saveAs = null;
	public JButton load = null;
	public JButton newDir = null;
	public JButton newLink = null;
	public JButton update = null;
	public JButton update2 = null;
	public JButton addSnapshot = null;
	public JButton deleteSnapshot = null;
	public DNDTree tree = null;
  //  @jve:decl-index=0:
	
	public PropertyTextPanel name;
	public PropertyTextPanel url;
	public PropertyTextAreaPanel description = null;
	public LinkText linkDescription = null;
	
	
	 JPanel jContentPane = null;
	 JPanel MainPanel = null;
	 JPanel TreePane = null;
	 JPanel Buttons = null;
	 JScrollPane TreeScroll = null;

	private JPanel actonPanel = null;
	private JPanel meta = null;

	private JPanel snapshots = null;
	private JPanel snapBtns = null;

	private JScrollPane scrollSnapList = null;

	public JTabbedPane metaTabs = null;

	private JPanel descriptionPane = null;

	public JPanel main = null;

	private JTabbedPane tools = null;

	private JPanel jPanel = null;

	public JButton exitBtn = null;

	private JPanel jPanel1 = null;

	public JScrollPane jScrollPane = null;

	public JTextField jTextField = null;

	public JButton jButtonSearch = null;

	public JList jList = null;

	public ExtendTree snapTree = null;

	public JButton snapDir = null;

	public JButton updateB = null;

	/**
	 * This is the default constructor
	 */
	public AppFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(782, 600);
		this.setPreferredSize(new Dimension(782, 600));
		this.setMinimumSize(new Dimension(600, 500));
		this.setContentPane(getJContentPane());
		this.setTitle("Link Collector");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), BorderLayout.NORTH);
			jContentPane.add(getTreePane(), BorderLayout.CENTER);
			jContentPane.add(getActonPanel(), BorderLayout.EAST);
		}
		return jContentPane;
	}

	/**
	 * This method initializes MainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (MainPanel == null) {
			MainPanel = new JPanel();
			MainPanel.setLayout(new BoxLayout(getMainPanel(), BoxLayout.X_AXIS));
			MainPanel.setPreferredSize(new Dimension(50, 50));
			MainPanel.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
			MainPanel.add(getSaveAs(), null);
			MainPanel.add(getLoad(), null);
			MainPanel.add(getJPanel(), null);
			MainPanel.add(getExitBtn(), null);
		}
		return MainPanel;
	}

	/**
	 * This method initializes TreePane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTreePane() {
		if (TreePane == null) {
			TreePane = new JPanel();
			TreePane.setLayout(new BorderLayout());
			TreePane.add(getButtons(), BorderLayout.NORTH);
			TreePane.add(getTreeScroll(), BorderLayout.CENTER);
		}
		return TreePane;
	}

	/**
	 * This method initializes Buttons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtons() {
		if (Buttons == null) {
			Buttons = new JPanel();
			Buttons.setLayout(new BoxLayout(getButtons(), BoxLayout.X_AXIS));
			Buttons.setPreferredSize(new Dimension(10, 30));
			Buttons.add(getNewDir(), null);
			Buttons.add(getNewLink(), null);
		}
		return Buttons;
	}

	/**
	 * This method initializes TreeScroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTreeScroll() {
		if (TreeScroll == null) {
			TreeScroll = new JScrollPane();
			TreeScroll.setViewportView(getTree());
		}
		return TreeScroll;
	}

	/**
	 * This method initializes saveAs	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSaveAs() {
		if (saveAs == null) {
			saveAs = new JButton();
			saveAs.setText("Save As...");
		}
		return saveAs;
	}

	/**
	 * This method initializes load	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLoad() {
		if (load == null) {
			load = new JButton();
			load.setText("Load");
		}
		return load;
	}

	/**
	 * This method initializes newDir	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNewDir() {
		if (newDir == null) {
			newDir = new JButton();
			newDir.setText("New Dir");
		}
		return newDir;
	}

	/**
	 * This method initializes newLink	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNewLink() {
		if (newLink == null) {
			newLink = new JButton();
			newLink.setText("New Link");
		}
		return newLink;
	}

	/**
	 * This method initializes actonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getActonPanel() {
		if (actonPanel == null) {
			actonPanel = new JPanel();
			actonPanel.setLayout(null);
			actonPanel.setPreferredSize(new Dimension(280, 50));
			actonPanel.add(getMetaTabs(), null);
			actonPanel.add(getTools(), null);
		}
		return actonPanel;
	}

	/**
	 * This method initializes meta	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMeta() {
		if (meta == null) {
			linkDescription = new LinkText();
			linkDescription.setBounds(new Rectangle(3, 85, 182, 13));
			linkDescription.setFont(new Font("Dialog", Font.PLAIN, 12));
			linkDescription.setText("description");
			meta = new JPanel();
			meta.setLayout(null);
			meta.setBounds(new Rectangle(9, 8, 253, 99));
			
			name = new PropertyTextPanel("name");
			name.setBounds(new Rectangle(2, 3, 252, 44));
			url = new PropertyTextPanel("url");
			url.setBounds(new Rectangle(3, 39, 251, 44));
			meta.add(name, null);
			meta.add(url, null);
			meta.add(linkDescription, null);
		}
		return meta;
	}

	/**
	 * This method initializes update	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getUpdate() {
		if (update == null) {
			update = new JButton();
			update.setText("Update");
			update.setBounds(new Rectangle(188, 109, 74, 26));
		}
		return update;
	}

	/**
	 * This method initializes snapshots	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSnapshots() {
		if (snapshots == null) {
			snapshots = new JPanel();
			snapshots.setLayout(null);

			snapshots.setPreferredSize(new Dimension(50, 50));
			snapshots.add(getSnapBtns(), null);
			snapshots.add(getScrollSnapList(), null);
		}
		return snapshots;
	}

	/**
	 * This method initializes snapBtns	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSnapBtns() {
		if (snapBtns == null) {
			snapBtns = new JPanel();
			snapBtns.setLayout(new BoxLayout(getSnapBtns(), BoxLayout.X_AXIS));
			snapBtns.setBounds(new Rectangle(6, 4, 254, 40));
			snapBtns.add(getSnapDir(), null);
			snapBtns.add(getAddSnapshot(), null);
			snapBtns.add(getUpdateB(), null);
			snapBtns.add(getDeleteSnapshot(), null);
		}
		return snapBtns;
	}

	/**
	 * This method initializes addSnapshot	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddSnapshot() {
		if (addSnapshot == null) {
			addSnapshot = new JButton();
			addSnapshot.setText("Add");
		}
		return addSnapshot;
	}

	/**
	 * This method initializes deleteSnapshot	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDeleteSnapshot() {
		if (deleteSnapshot == null) {
			deleteSnapshot = new JButton();
			deleteSnapshot.setText("Delete");
		}
		return deleteSnapshot;
	}

	/**
	 * This method initializes scrollSnapList	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollSnapList() {
		if (scrollSnapList == null) {
			scrollSnapList = new JScrollPane();
			scrollSnapList.setBounds(new Rectangle(6, 49, 254, 241));
			scrollSnapList.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			scrollSnapList.setViewportView(getSnapTree());
			scrollSnapList.setPreferredSize(new Dimension(126, 26));
		}
		return scrollSnapList;
	}

	/**
	 * This method initializes tree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getTree() {
		if (tree == null) {
			tree = new DNDTree();
		}
		return tree;
	}

	public void initTree(Object rootData) {
		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(rootData,true), false));
		tree.setRootVisible(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	/**
	 * This method initializes metaTabs	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getMetaTabs() {
		if (metaTabs == null) {
			metaTabs = new JTabbedPane();
			metaTabs.setTabPlacement(JTabbedPane.TOP);
			metaTabs.setBounds(new Rectangle(6, 8, 274, 170));
			metaTabs.addTab("main info", null, getMain(), null);
			metaTabs.addTab("description", null, getDescriptionPane(), null);
		}
		return metaTabs;
	}

	/**
	 * This method initializes descriptionPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDescriptionPane() {
		if (descriptionPane == null) {
			descriptionPane = new JPanel();
			descriptionPane.setLayout(null);
			descriptionPane.add(getUpdate2(), null);
			descriptionPane.add(getDescription(), null);
		}
		return descriptionPane;
	}

	/**
	 * This method initializes main	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMain() {
		if (main == null) {
			main = new JPanel();
			main.setLayout(null);
			main.add(getMeta(), null);
			main.add(getUpdate(), null);
		}
		return main;
	}

	/**
	 * This method initializes tools	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTools() {
		if (tools == null) {
			tools = new JTabbedPane();
			tools.setBounds(new Rectangle(7, 191, 273, 332));
			tools.addTab("Snapshots", null, getSnapshots(), null);
			tools.addTab("Search", null, getJPanel1(), null);
		}
		return tools;
	}



	/**
	 * This method initializes update2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getUpdate2() {
		if (update2 == null) {
			update2 = new JButton();
			update2.setText("Update");
			update2.setLocation(new Point(188, 109));
			update2.setSize(new Dimension(74, 26));
		}
		return update2;
	}

	/**
	 * This method initializes description	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDescription() {
		if (description == null) {
			description = new PropertyTextAreaPanel(null);
			description.setBounds(new Rectangle(5, 6, 257, 99));
		}
		return description;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			jPanel = new JPanel();
			jPanel.setLayout(gridLayout);
		}
		return jPanel;
	}

	/**
	 * This method initializes exitBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getExitBtn() {
		if (exitBtn == null) {
			exitBtn = new JButton();
			exitBtn.setText("Exit");
		}
		return exitBtn;
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
			jPanel1.add(getJScrollPane(), null);
			jPanel1.add(getJTextField(), null);
			jPanel1.add(getJButtonSearch(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(9, 48, 245, 247));
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setBounds(new Rectangle(9, 12, 199, 26));
		}
		return jTextField;
	}

	/**
	 * This method initializes jButtonSearch	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSearch() {
		if (jButtonSearch == null) {
			jButtonSearch = new JButton();
			jButtonSearch.setBounds(new Rectangle(214, 16, 44, 22));
			jButtonSearch.setText(">");
		}
		return jButtonSearch;
	}

	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJList() {
		if (jList == null) {
			jList = new JList();
		}
		return jList;
	}

	/**
	 * This method initializes snapTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getSnapTree() {
		if (snapTree == null) {
			snapTree = new ExtendTree();
		}
		return snapTree;
	}

	/**
	 * This method initializes snapDir	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSnapDir() {
		if (snapDir == null) {
			snapDir = new JButton();
			snapDir.setText("Dir");
		}
		return snapDir;
	}

	/**
	 * This method initializes updateB	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getUpdateB() {
		if (updateB == null) {
			updateB = new JButton();
			updateB.setText("Update");
		}
		return updateB;
	}

}
