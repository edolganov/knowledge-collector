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
        boolean selectedState;
    	
        public CloseButton(boolean canClose) {
            initUI();
            this.canClose = canClose;
            if(!canClose)setVisible(false);
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
            	if(!selectedState){
            		setIcon(passive);
            	}else {
            		setIcon(active);
            	}
            } else {
            	setIcon(null);
            }
        }

		public void setSelectedState(boolean b) {
			this.selectedState = b;
			if(b) 
				setActive();
			else 
				setPassive();
		}

    }
	
    private static Icon active = IconUtil.get("/ru/kc/platform/ui/img/close-active.png");
	private static Icon passive = IconUtil.get("/ru/kc/platform/ui/img/close-passive.png");
	
	private JLabel label;
	private String text;
	private boolean bold;
	private boolean modified;
	
	private CloseButton button;
	
	
    
    public TabHeader(String text, boolean canClose) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        
        label = new JLabel();
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(label);

        button = new CloseButton(canClose);
        add(button);

		setText(text);
	}
    

	public void addActionListener(ActionListener l) {
    	button.addActionListener(l);
    }
	

    
    public void setText(String text) {
    	this.text = text;
    	printText();
    }
    
	
	public void setBold(boolean value) {
		this.bold = value;
		printText();
	}
	
    private void printText(){
    	String text = this.text;
    	if(modified){
    		text = "* "+text; 
    	}
    	
    	if(bold){
    		label.setText("<html><b>"+text+"</b></html>");
    	} else {
    		label.setText(text);
    	}
    }
	
    
    public String getText() {
        return text;
    }


	public void setSelected(boolean value) {
		button.setSelectedState(value);
		setBold(value);
	}

	public void setModified(boolean value) {
		this.modified = value;
		printText();
	}













}
