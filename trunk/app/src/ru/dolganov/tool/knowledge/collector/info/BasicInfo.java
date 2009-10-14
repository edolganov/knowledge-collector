package ru.dolganov.tool.knowledge.collector.info;

import javax.swing.JPanel;
import java.awt.Rectangle;

import ru.chapaj.tool.link.collector.ui.component.PropertyTextAreaPanel;
import ru.chapaj.tool.link.collector.ui.component.PropertyTextPanel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Point;

public class BasicInfo extends JPanel {

	private static final long serialVersionUID = 1L;
	public PropertyTextPanel name = null;
	public PropertyTextAreaPanel description = null;
	public JButton jButton = null;
	public JButton wrapB = null;

	/**
	 * This is the default constructor
	 */
	public BasicInfo() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(null);
		this.setBounds(new Rectangle(0, 0, 332, 459));
		this.add(getJPanel(), null);
		this.add(getJPanel2(), null);
		this.add(getJButton(), null);
		this.add(getWrapB(), null);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getJPanel() {
		if (name == null) {
			name = new PropertyTextPanel();
			name.setBounds(new Rectangle(3, 5, 327, 35));
			name.setText("");
		}
		return name;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getJPanel2() {
		if (description == null) {
			description = new PropertyTextAreaPanel();
			description.setBounds(new Rectangle(3, 46, 327, 152));
		}
		return description;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(242, 203, 88, 26));
			jButton.setText("update");
		}
		return jButton;
	}

	/**
	 * This method initializes wrapB	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getWrapB() {
		if (wrapB == null) {
			wrapB = new JButton();
			wrapB.setText("wrap");
			wrapB.setLocation(new Point(3, 203));
			wrapB.setSize(new Dimension(70, 26));
			wrapB.setPreferredSize(new Dimension(70, 26));
		}
		return wrapB;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
