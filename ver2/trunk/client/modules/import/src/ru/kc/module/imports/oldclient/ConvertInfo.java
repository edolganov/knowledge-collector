package ru.kc.module.imports.oldclient;

import ru.kc.model.Node;
import ru.kc.module.imports.oldclient.oldmodel.RootElement;

public class ConvertInfo {
	
	public final Node parent;
	public final RootElement oldNode;
	public final Node node;
	public final String textForTextNode;
	
	public ConvertInfo(Node parent, RootElement oldNode, Node node) {
		this(parent, oldNode, node, null);
	}

	public ConvertInfo(Node parent, RootElement oldNode, Node node, String textForTextNode) {
		this.parent = parent;
		this.oldNode = oldNode;
		this.node = node;
		this.textForTextNode = textForTextNode;
	}
	
	

}
