package model.knowledge;

import ru.chapaj.util.UuidGenerator;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import model.HavingUuid;
import model.knowledge.role.Parent;

/**
 * Элемент рута
 * @author jenua.dolganov
 *
 */
public abstract class RootElement implements Parent, HavingUuid {

	protected String uuid;
	protected Long createDate;
	@XStreamOmitField
	private Root parent;

	public RootElement() {
		super();
	}

	public Root getParent() {
		return parent;
	}

	public void setParent(Root parent) {
		this.parent = parent;
	}

	public String getUuid() {
		if(uuid == null) {
			uuid = UuidGenerator.simpleUuid();
		}
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!getClass().equals(obj.getClass())) return false;
		if(!getUuid().equals(((RootElement)obj).getUuid())) return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getName() +": " + getUuid();
	}

}