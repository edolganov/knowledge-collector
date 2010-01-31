package ru.chapaj.util.swing.menu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class SelectAllAction extends AbstractAction{ 
    JTextComponent comp; 
 
    public SelectAllAction(JTextComponent comp){ 
        super("Select All"); 
        this.comp = comp; 
    } 
 
    public void actionPerformed(ActionEvent e){ 
        comp.selectAll(); 
    } 
 
    public boolean isEnabled(){ 
        return comp.isEnabled() 
                && comp.getText().length()>0; 
    } 
} 
