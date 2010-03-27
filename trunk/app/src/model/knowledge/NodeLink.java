package model.knowledge;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Ссылка на ноду
 * @author jenua.dolganov
 */
@XStreamAlias("node-link")
public class NodeLink extends RootElement {
	
	String nodeUuid;

	public String getNodeUuid() {
		return nodeUuid;
	}

	public void setNodeUuid(String nodeUuid) {
		this.nodeUuid = nodeUuid;
	}
	
}
