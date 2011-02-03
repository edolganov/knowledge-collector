package ru.kc.platform.ui.tabs;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import ru.kc.util.swing.icon.IconUtil;

import java.awt.*;
import java.awt.event.*;


public class TabButton extends JPanel {
	
    private static class CloseButton extends JButton {
    	
        private static class MouseListenerImpl extends MouseAdapter {
        	
        	CloseButton parent;
        	
            public MouseListenerImpl(CloseButton parent) {
				super();
				this.parent = parent;
			}

			public void mouseEntered(MouseEvent e) {
				parent.setIcon(active);
            }

            public void mouseExited(MouseEvent e) {
            	if(!parent.selected)parent.setIcon(passive);
            	else parent.setIcon(active);
            	
            }
        };
        
        boolean selected;
    	
        public CloseButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Close");
            setUI(new BasicButtonUI());
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            setRolloverEnabled(true);
            addMouseListener(new MouseListenerImpl(this));
            setIcon(passive);
        }


        public void updateUI() {}


		public void setSelected(boolean b) {
			selected = b;
			if(selected){
				setIcon(active);
			}else{
				setIcon(passive);
			}
			
		}
    }
	
	static Icon active = IconUtil.get("/ru/kc/platform/ui/img/close-active.png");
	static Icon passive = IconUtil.get("/ru/kc/platform/ui/img/close-passive.png");
	
	JLabel label  = new JLabel();
	String text;
	String textPreffix;
	boolean textPreffixVisible;
	boolean bold = false;
	
	CloseButton button = new CloseButton();
	boolean silentMode = false;
	
    public TabButton() {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);

        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        
        add(button);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }
    
    public TabButton(String text) {
		this();
		setText(text);
	}
    

	public void addActionListener(ActionListener l) {
    	button.addActionListener(l);
    }
	

    
    public void setText(String text) {
    	this.text = text;
        label.setText(text);
    }
    
	public void setTextPreffix(String textPreffix) {
		this.textPreffix = textPreffix;
	}
	
	public void setTextPreffixVisible(boolean visible) {
		if(visible && !textPreffixVisible){
			label.setText(textPreffix+text);
			textPreffixVisible = true;
		} else if(!visible && textPreffixVisible){
			label.setText(text);
			textPreffixVisible = false;
		}
	}
	
	public void setBold(boolean visible) {
		if(visible && !bold){
			label.setText("<html><b>"+label.getText()+"</b></html>");
			bold = true;
		} else if(!visible && bold){
			setTextPreffixVisible(textPreffixVisible);
			bold = false;
		}
		
	}
	
    
    public String getText() {
        return label.getText();
    }














}
