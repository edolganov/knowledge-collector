package ru.kc.module.dashboard.tools;
import java.awt.*;
import java.beans.*;
import javax.swing.*;
 
public class SplitPaneActionTest implements PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent e) {
        //System.out.printf("The %s property changed from %s to %s%n",
        //           e.getPropertyName(), e.getOldValue(), e.getNewValue());
        JSplitPane splitPane = (JSplitPane)e.getSource();
        int loc = ((Integer)e.getNewValue()).intValue();
        int size = splitPane.getDividerSize();
        int width = splitPane.getWidth();
        if(loc == 1 || loc+size+1 == width)
            System.out.printf("Divider collapsed %s%n",
                               (loc == 1) ? "left" : "right");
    }
 
    private JSplitPane getContent() {
        JSplitPane splitPane = new JSplitPane();
        String prop = JSplitPane.DIVIDER_LOCATION_PROPERTY;
        splitPane.addPropertyChangeListener(prop, this);
        splitPane.setLeftComponent(getPanel(Color.yellow));
        splitPane.setRightComponent(getPanel(Color.magenta));
        splitPane.setDividerLocation(200);
        splitPane.setOneTouchExpandable(true);
        return splitPane;
    }
 
    private JPanel getPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(200,200));
        return panel;
    }
 
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(new SplitPaneActionTest().getContent());
        f.setSize(400,200);
        f.setVisible(true);
    }
}

