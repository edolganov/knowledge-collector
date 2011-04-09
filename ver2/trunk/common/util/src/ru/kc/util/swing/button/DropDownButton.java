package ru.kc.util.swing.button;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DropDownButton extends JToggleButton {

	private static final long serialVersionUID = 2857744715416733620L;
	
	private JPopupMenu menu;
	
	public DropDownButton(Action a) {
		super(a);
		init();
	}

	public DropDownButton(Icon icon, boolean selected) {
		super(icon, selected);
		init();
	}

	public DropDownButton(Icon icon) {
		super(icon);
		init();
	}

	public DropDownButton(String text, boolean selected) {
		super(text, selected);
		init();
	}

	public DropDownButton(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		init();
	}

	public DropDownButton(String text, Icon icon) {
		super(text, icon);
		init();
	}

	public DropDownButton(String text) {
		super(text);
		init();
	}

	public DropDownButton() {
		init();
	}

    private void init() {
        addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            	if(menu == null) return;
                boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
				if (isSelected) {
            		menu.show(DropDownButton.this, 0, getHeight());
                }
            }
        });
	}

	public JPopupMenu getMenu() {
		return menu;
	}

	public void setMenu(JPopupMenu menu) {
		this.menu = menu;
		initMenu();
	}

	private void initMenu() {
        menu.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            	deselectButtonRequest();
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            	deselectButtonRequest();
            }
        });
	}

	private void deselectButtonRequest() {
		setSelected(false);
	}

	//test
    public static void main(String[] arguments) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Toolbar with Popup Menu demo");

                final JToolBar toolBar = new JToolBar();
                JPopupMenu menu = new JPopupMenu();
                menu.add(new JMenuItem("Black"));
                DropDownButton button = new DropDownButton();
                button.setMenu(menu);
                button.setText("test");
				toolBar.add(button);

                final JPanel panel = new JPanel(new BorderLayout());
                panel.add(toolBar, BorderLayout.NORTH);
                panel.setPreferredSize(new Dimension(600, 400));
                frame.getContentPane().add(panel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

            }
        });
    }
}
