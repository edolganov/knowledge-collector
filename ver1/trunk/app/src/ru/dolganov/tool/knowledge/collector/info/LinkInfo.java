package ru.dolganov.tool.knowledge.collector.info;

import javax.swing.JPanel;
import java.awt.Rectangle;

import ru.chapaj.tool.link.collector.ui.component.PropertyTextAreaPanel;
import ru.chapaj.tool.link.collector.ui.component.PropertyTextPanel;
import javax.swing.JButton;
import java.awt.Dimension;

public class LinkInfo extends JPanel {

	private static final long serialVersionUID = 1L;
	public PropertyTextPanel name = null;
	public PropertyTextAreaPanel description = null;
	public JButton jButton = null;
	public PropertyTextPanel url = null;
	public JButton wrapB = null;

	/**
	 * This is the default constructor
	 */
	public LinkInfo() {
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
		this.add(getUrl(), null);
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
			name.setBounds(new Rectangle(3, 5, 326, 35));
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
			description.setBounds(new Rectangle(3, 93, 326, 152));
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
			jButton.setBounds(new Rectangle(241, 252, 88, 26));
			jButton.setPreferredSize(new Dimension(70, 26));
			jButton.setText("update");
		}
		return jButton;
	}

	/**
	 * This method initializes url	
	 * 	
	 * @return ru.chapaj.tool.link.collector.ui.component.PropertyTextPanel	
	 */
	private PropertyTextPanel getUrl() {
		if (url == null) {
			url = new PropertyTextPanel();
			url.setBounds(new Rectangle(3, 47, 326, 35));
			url.setText("");
		}
		return url;
	}

	/**
	 * This method initializes wrapB	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getWrapB() {
		if (wrapB == null) {
			wrapB = new JButton();
			wrapB.setBounds(new Rectangle(3, 252, 75, 26));
			wrapB.setPreferredSize(new Dimension(70, 26));
			wrapB.setText("wrap");
		}
		return wrapB;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
