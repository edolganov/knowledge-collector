package ru.kc.common.dnd;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.StringReader;

import ru.kc.model.Node;

//like java.awt.datatransfer.StringSelection
public class NodeSelection implements Transferable, ClipboardOwner {

	private static final int STRING = 0;
	private static final int PLAIN_TEXT = 1;
	private static final int NODE = 2;

	private static final DataFlavor[] flavors = { DataFlavor.stringFlavor,
			DataFlavor.plainTextFlavor, // deprecated
			new DataFlavor(Node.class, "node") };

	private Node node;

	public NodeSelection(Node node) {
		this.node = node;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return (DataFlavor[]) flavors.clone();
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (int i = 0; i < flavors.length; i++) {
			if (flavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(flavors[NODE])) {
			return node;
		} 
		
		if (flavor.equals(flavors[STRING])) {
			return node.getName();
		} 
		
		if (flavor.equals(flavors[PLAIN_TEXT])) {
			return new StringReader(node.getName());
		} 
		
		throw new UnsupportedFlavorException(flavor);
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) {}

}
