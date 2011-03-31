package ru.kc.util.swing.tree;

import java.awt.datatransfer.DataFlavor;

public class TransferInfo {
	
	public final DataFlavor dataFlavor;
	public final Object data;
	
	public TransferInfo(DataFlavor dataFlavor, Object data) {
		super();
		this.dataFlavor = dataFlavor;
		this.data = data;
	}

}
