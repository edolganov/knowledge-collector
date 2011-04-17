/**
 * Copyright (c) 2009, Piet Blok
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
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CheckerboardPainter;

/**
 * A test application specifically designed to test the cooperation of
 * RepaintManagers.
 * <p>
 * Run a web start demo: <a
 * href="http://www.pbjar.org/blogs/jxlayer/jxlayer40/SwingXDemo.jnlp"> <IMG
 * style="CLEAR: right" alt="Web Start Shared JXLayer"
 * src="http://javadesktop.org/javanet_images/webstart.small2.gif"
 * align="middle" border="1" /> </a>
 * </p>
 * 
 * @author Piet Blok
 */
public class TestSwingX implements TestGUI {

    /**
     * Run the demo.
     * 
     * @param args
     *            not used
     */
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		new TestSwingX().createGUI();
	    }
	});
    }

    private final JXFrame frame = new JXFrame(
	    "SwingX and SimpleBufferedLayerUI");

    private static final float alphaHigh = 0.99f, alphaLow = 0.4f;

    private final JScrollPane scroller = new JScrollPane();

    private  JXLayer<?> layer;

    @Override
    public void repaintGUI(boolean force) {
	if (force) {
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
    }

    @Override
    public void setScroller(boolean scrollPane) {
	if (scrollPane) {
	    frame.getContentPane().remove(layer);
	    this.scroller.setViewportView(layer);
	    frame.getContentPane().add(this.scroller, BorderLayout.CENTER);
	} else {
	    frame.getContentPane().remove(scroller);
	    frame.getContentPane().add(layer, BorderLayout.CENTER);
	}
	layer.revalidate();
	layer.repaint();
	repaintGUI(false);

    }

    private JComponent createContent() {
	final JPanel content = new JPanel(new BorderLayout());
	JXPanel xPanel = new JXPanel(new GridLayout(3, 0)) {

	    private static final long serialVersionUID = 1L;

	    private Shape shape = null;

	    private Font myFont = null;

	    private void createShape(Graphics2D g2, String message) {
		Rectangle inner = new Rectangle();
		SwingUtilities.calculateInnerArea(this, inner);

		myFont = g2.getFont().deriveFont(Font.BOLD);

		GlyphVector myGlyph = myFont.createGlyphVector(g2
			.getFontRenderContext(), message);
		Rectangle2D glyphRect = myGlyph.getVisualBounds();

		Area area = new Area(myGlyph.getOutline((float) (inner
			.getCenterX() - glyphRect.getCenterX()), (float) (inner
			.getCenterY() - glyphRect.getCenterY())));

		Rectangle2D areaBounds = area.getBounds2D();

		AffineTransform rotator = new AffineTransform();
		rotator.rotate(Math.PI / -4.0, areaBounds.getCenterX(),
			areaBounds.getCenterY());
		area.transform(rotator);
		Rectangle2D newBounds = area.getBounds2D();
		double scale = Math.min(
			inner.getWidth() / newBounds.getWidth(), inner
				.getHeight()
				/ newBounds.getHeight());
		try {
		    area.transform(rotator.createInverse());
		} catch (NoninvertibleTransformException e) {
		    e.printStackTrace();
		}
		AffineTransform scaler = new AffineTransform();
		scaler.translate(areaBounds.getCenterX(), areaBounds
			.getCenterY());
		scaler.scale(scale, scale);
		scaler.translate(-areaBounds.getCenterX(), -areaBounds
			.getCenterY());
		area.transform(scaler);
		area.transform(rotator);
		shape = area;
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		if (shape == null) {
		    createShape(g2, "JXPanel");
		}
		g2.setFont(myFont);
		g2.setColor(Color.CYAN);
		g2.fill(shape);
		g2.setColor(Color.YELLOW);
		g2.draw(shape);
	    }

	};
//	xPanel.setOpaque(true);
	content.add(xPanel, BorderLayout.CENTER);
	for (int index = 0; index < 3 * 3; index++) {
	    final JXPanel item = new JXPanel(new BorderLayout());
	    item.setBorder(BorderFactory.createCompoundBorder(BorderFactory
		    .createBevelBorder(BevelBorder.RAISED), BorderFactory
		    .createBevelBorder(BevelBorder.LOWERED)));
	    xPanel.add(item, BorderLayout.CENTER);
	    JXBusyLabel busyLabel = new JXBusyLabel();
	    item.add(busyLabel);
	    busyLabel.setBusy(true);
	    busyLabel.getBusyPainter().setHighlightColor(Color.MAGENTA);
	    item.setAlpha(alphaLow);
	    MouseAdapter adapter = new MouseAdapter() {

		@Override
		public void mouseEntered(MouseEvent e) {
		    item.setAlpha(alphaHigh);
		}

		@Override
		public void mouseExited(MouseEvent e) {
		    item.setAlpha(alphaLow);
		}

	    };
	    item.addMouseListener(adapter);
	}
	xPanel.setBackgroundPainter(new CheckerboardPainter(Color.CYAN,
		Color.YELLOW, xPanel.getPreferredSize().width / 2));
	return content;
    }

    private void createGUI() {
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.add(scroller);
	layer = TransformUtils
	    .createTransformJXLayer(createContent(), 4.0,
		    new QualityHints());
	scroller.setViewportView(layer);
	JDialog controlDialog = new ControlDialog(layer, this);
	controlDialog.pack();
	controlDialog.setVisible(true);
	repaintGUI(true);
    }
}
