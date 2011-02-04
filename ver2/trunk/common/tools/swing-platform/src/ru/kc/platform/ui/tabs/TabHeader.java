package ru.kc.platform.ui.tabs;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import ru.kc.util.swing.icon.IconUtil;

import java.awt.*;
import java.awt.event.*;


public class TabHeader extends JPanel {
	
    private static class CloseButton extends JButton {
    	
        private static class MouseListenerImpl extends MouseAdapter {
       
        	CloseButton parent;
        	
            public MouseListenerImpl(CloseButton parent) {
				super();
				this.parent = parent;
			}

			public void mouseEntered(MouseEvent e) {
				parent.setActive();
            }

            public void mouseExited(MouseEvent e) {
            	parent.setPassive();
            }
        };
        
        
        boolean canClose;
    	
        public CloseButton(boolean canClose) {
            initUI();
            this.canClose = canClose;
            setPassive();
        }

		private void initUI() {
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
		}
        
        public void setActive(){
            if(canClose){
            	setIcon(active);
            } else {
            	setIcon(null);
            }
        }
        
        public void setPassive(){
            if(canClose){
            	setIcon(passive);
            } else {
            	setIcon(null);
            }
        }

    }
	
    private static Icon active = IconUtil.get("/ru/kc/platform/ui/img/close-active.png");
	private static Icon passive = IconUtil.get("/ru/kc/platform/ui/img/close-passive.png");
	
	private JLabel label  = new JLabel();
	private String text;
	private String textPreffix;
	private boolean textPreffixVisible;
	private boolean bold = false;
	
	private CloseButton button;
	
    
    public TabHeader(String text, boolean canClose) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        
        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        
        button = new CloseButton(canClose);
        add(button);

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
