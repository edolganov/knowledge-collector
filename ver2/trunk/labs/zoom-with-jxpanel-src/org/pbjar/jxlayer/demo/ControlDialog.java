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
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.LayerUI;
import org.pbjar.jxlayer.plaf.ext.TransformUI;
import org.pbjar.jxlayer.repaint.RepaintManagerUtils;

/**
 * A control dialog for the Transformation demo's.
 * 
 * @author Piet Blok
 */
public class ControlDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private enum X {
	enableUIAction,

	useScrollAction,

	autoPackAction,

	packNowAction,

	rpmTree;
    }

    private final TransformUI transformUI;

    /**
     * Create a ControlDialog.
     * 
     * @param layer
     *            a JXLayer with BufferedTransformPort and
     *            SimpleBufferredLayerUI set
     * @param testframe
     *            the demo application
     */
    public ControlDialog(final JXLayer<?> layer, final TestGUI testframe) {
	super((JFrame) null, "Control transform");
	transformUI = (TransformUI) layer.getUI();
	/*
	 * Create the required actions in an array.
	 */
	final Action[] actions = new Action[X.values().length];

	actions[X.enableUIAction.ordinal()] = new AbstractAction("Set UI") {

	    private static final long serialVersionUID = 1L;

	    {
		this.putValue(Action.SELECTED_KEY, layer.getUI() != null);
	    }

	    @SuppressWarnings("unchecked")
	    @Override
	    public void actionPerformed(ActionEvent e) {
		layer
			.setUI(((Boolean) this.getValue(Action.SELECTED_KEY)) ? (LayerUI) transformUI
				: null);
	    }
	};

	actions[X.autoPackAction.ordinal()] = new AbstractAction("Auto pack") {

	    private static final long serialVersionUID = 1L;

	    {
		this.putValue(Action.SELECTED_KEY, false);
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
		testframe.repaintGUI((Boolean) this
			.getValue(Action.SELECTED_KEY));
	    }
	};

	actions[X.useScrollAction.ordinal()] = new AbstractAction("Scroll bars") {

	    private static final long serialVersionUID = 1L;

	    {
		this.putValue(Action.SELECTED_KEY, true);
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
		boolean scroll = (Boolean) this.getValue(Action.SELECTED_KEY);
		testframe.setScroller(scroll);
		testframe.repaintGUI((Boolean) actions[X.autoPackAction
			.ordinal()].getValue(Action.SELECTED_KEY));
	    }
	};

	actions[X.packNowAction.ordinal()] = new AbstractAction("Pack") {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		testframe.repaintGUI(true);
	    }
	};

	actions[X.rpmTree.ordinal()] = RepaintManagerUtils
		.createRPDisplayAction();

	/*
	 * Create a ToolBar with the actions.
	 */
	JToolBar toolBar = new JToolBar();
	this.add(toolBar, BorderLayout.PAGE_START);
	for (Action action : actions) {
	    if (action.getValue(Action.SELECTED_KEY) == null) {
		toolBar.add(action);
	    } else {
		toolBar.add(new JCheckBox(action));
	    }
	}
	/*
	 * Create a general property change listener.
	 */
	PropertyChangeListener listener = new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent evt) {
		SwingUtilities.invokeLater(new Runnable() {

		    @Override
		    public void run() {
			testframe.repaintGUI((Boolean) actions[X.autoPackAction
				.ordinal()].getValue(Action.SELECTED_KEY));
		    }
		});
	    }
	};

	TransformWheelButtons twbs = new TransformWheelButtons(layer, listener);
	this.add(twbs, BorderLayout.CENTER);
	toolBar.add(twbs.getTipDisplayButton());
    }
}
