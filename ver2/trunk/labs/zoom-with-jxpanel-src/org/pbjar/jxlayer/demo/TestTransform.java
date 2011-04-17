/**
 * Copyright (c) 2008-2009, Piet Blok
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of the copyright holder nor the names of the
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.pbjar.jxlayer.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import org.jdesktop.jxlayer.JXLayer;
import org.pbjar.jxlayer.plaf.ext.TransformUI;
import org.pbjar.jxlayer.repaint.RepaintManagerUtils;

/**
 * Test the {@link TransformUI}.
 * 
 * <p>
 * Run a web start demo: <a
 * href="http://www.pbjar.org/blogs/jxlayer/jxlayer40/TransformDemo.jnlp"> <IMG
 * style="CLEAR: right" alt="Web Start Transformations"
 * src="http://javadesktop.org/javanet_images/webstart.small2.gif"
 * align="middle" border="1" /> </a>
 * </p>
 */
public class TestTransform implements TestGUI {

    private class ReferenceGUI {

	private final JDialog referenceDialog = new JDialog((JFrame) null,
		"Reference");

	private final JComponent referenceContent = createContent();

	private final JScrollPane referenceScrollPane = new JScrollPane();

	public ReferenceGUI() {
	    referenceDialog.setJMenuBar(new JMenuBar());
	    referenceDialog.getJMenuBar().add(
		    new JMenuItem(new AbstractAction("Pack") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
			    referenceDialog.pack();
			}
		    }));
	    referenceScrollPane.setViewportView(referenceContent);
	    referenceDialog.add(referenceScrollPane);
	    referenceDialog.pack();
	}

	public void removeScroll() {
	    referenceDialog.remove(referenceScrollPane);
	    referenceDialog.add(referenceContent);
	    pack();
	}

	public void setVisible(boolean visible) {
	    referenceDialog.setVisible(visible);
	}

	public void setVisible(Rectangle bounds) {
	    referenceDialog.setLocation(bounds.x + bounds.width + 5, bounds.y);
	    setVisible(true);
	}

	public void wrapIntoScroll() {
	    referenceDialog.remove(referenceContent);
	    referenceDialog.add(referenceScrollPane);
	    referenceScrollPane.setViewportView(referenceContent);
	    pack();
	}

	private void pack() {
	    referenceDialog.pack();
	}
    }

    /**
     * Run the program.
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) {
	try {
	    TransformUI.prepareForJTextComponent();
	} catch (Throwable t) {
	    while (t != null) {
		t.printStackTrace();
		t = t.getCause();
	    }
	    System.exit(0);
	}
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		new TestTransform().createGUI();
	    }
	});
    }

    private final JFrame testframe;

    private final JScrollPane testScrollPane;

    private final JXLayer<?> layer;

    private final JDialog controlDialog;

    private final ReferenceGUI referenceGUI;

    private static final Icon animatedIcon = new AnimatedIcon();

    private static void setBorderTitle(JComponent component, String title) {
	component.setBorder(new TitledBorder(new CompoundBorder(
		new BevelBorder(BevelBorder.RAISED), new BevelBorder(
			BevelBorder.LOWERED)), title));
    }

    private TestTransform() {
	testframe = new JFrame("SwingX & TransformUI");
	testScrollPane = new JScrollPane();
	layer = TransformUtils.createTransformJXLayer(createContent(), 2,
		new QualityHints());
	controlDialog = new ControlDialog(layer, this);
	referenceGUI = new ReferenceGUI();
    }

    @Override
    public void repaintGUI(boolean force) {
	if (force) {
	    testframe.pack();
	    testframe.setLocationRelativeTo(null);
	    testframe.setVisible(true);
	}
    }

    @Override
    public void setScroller(boolean value) {
	if (value) {
	    testframe.remove(layer);
	    testframe.add(testScrollPane);
	    testScrollPane.setViewportView(layer);

	    referenceGUI.wrapIntoScroll();
	} else {
	    testframe.remove(testScrollPane);
	    testframe.add(layer);

	    referenceGUI.removeScroll();
	}
	testframe.invalidate();
	testframe.validate();
	testframe.repaint();

	layer.repaint();
    }

    private JComponent createContent() {
	JPanel demoContent = new JPanel(new BorderLayout());
	JTextPane textPane = new JTextPane();
	textPane.setContentType("text/html");
	StringBuilder sb = new StringBuilder();
	sb.append("<html><h1>Demo</h1>");
	for (int index = 0; index < 36; index++) {
	    sb.append("<p>");
	    sb.append("Paragraph " + index + ": aaa bbb ccc ddd eee fff.");
	    sb.append("</p>");
	}
	sb.append("</html>");
	textPane.setText(sb.toString());
	JScrollPane textScroller = new JScrollPane(textPane);
	textScroller.setPreferredSize(new Dimension(100, 80));
	demoContent.add(textScroller, BorderLayout.CENTER);
	JPanel someTextPanel = new JPanel(new BorderLayout());
	demoContent.add(someTextPanel, BorderLayout.PAGE_START);
	JTextField someText = new JTextField("Some text");
	someTextPanel.add(someText, BorderLayout.CENTER);
	someTextPanel.add(new JLabel(animatedIcon), BorderLayout.LINE_START);
	final JTextField message = new JTextField("Message area", JLabel.CENTER);
	demoContent.add(message, BorderLayout.PAGE_END);
	JPanel buttonPanel = new JPanel(new GridLayout(0, 4));
	demoContent.add(buttonPanel, BorderLayout.LINE_START);
	for (int index = 1; index < 17; index++) {
	    buttonPanel.add(new JButton(new AbstractAction(String
		    .valueOf(index)) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
		    message.setText("Button "
			    + ((JButton) e.getSource()).getText() + " pressed");
		}
	    }));
	}
	setBorderTitle(demoContent, " Demo content ");
	return demoContent;
    }

    private void createGUI() {
	createTestFrame();
	controlDialog.pack();
	controlDialog.setVisible(true);
	Rectangle bounds = controlDialog.getBounds();
	referenceGUI.setVisible(bounds);
//	referenceGUI.setVisible(true);

	repaintGUI(true);
    }

    private void createTestFrame() {
	testframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	testScrollPane.setViewportView(layer);
	testframe.add(testScrollPane);
	testframe.setJMenuBar(new JMenuBar());
	JMenu menu = new JMenu("Options");
	testframe.getJMenuBar().add(menu);

	populateMenu(menu);
    }

    private void populateMenu(JMenu menu) {

	menu.add(new JMenuItem(new AbstractAction(
		"Show SimpleBufferedLayerUI control panel") {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		controlDialog.setVisible(true);
	    }
	}));

	menu.add(new JMenuItem(new AbstractAction("Show reference frame") {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		referenceGUI.setVisible(true);
	    }
	}));

	menu.add(RepaintManagerUtils.createRPDisplayAction());
    }

}
