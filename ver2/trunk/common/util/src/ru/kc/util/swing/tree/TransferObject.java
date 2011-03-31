package ru.kc.util.swing.tree;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

//like java.awt.datatransfer.StringSelection
public class TransferObject implements Transferable, ClipboardOwner {

	private TransferInfo[] infos;

	public TransferObject(TransferInfo... infos) {
		if(infos == null) infos = new TransferInfo[0];
		this.infos = infos;
	}

	public DataFlavor[] getTransferDataFlavors() {
		try {
			
			DataFlavor[] out = new DataFlavor[infos.length];
			for (int i = 0; i < infos.length; i++) {
				out[i] = (DataFlavor)infos[i].dataFlavor.clone();
			}
			return out;
			
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (int i = 0; i < infos.length; i++) {
			if (flavor.equals(infos[i].dataFlavor)) {
				return true;
			}
		}
		return false;
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException{
		
		for (int i = 0; i < infos.length; i++) {
			if (flavor.equals(infos[i].dataFlavor)) {
				return infos[i].data;
			}
		}
		
		throw new UnsupportedFlavorException(flavor);
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) {}

}
