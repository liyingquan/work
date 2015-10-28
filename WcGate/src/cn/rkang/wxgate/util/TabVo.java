package cn.rkang.wxgate.util;

import org.springframework.beans.BeanUtils;
import org.zkoss.zul.Tab;

/**
 * 页签
 */
public class TabVo {
	String	id;
	boolean	selected;
	boolean	closable;
	String	iconSclass;
	String	label;

	Tab		tab	= null;

	public Tab cloneTab() {
		if (tab == null) {
			tab = new Tab();
			BeanUtils.copyProperties(this, tab);
		}
		return tab;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getIconSclass() {
		return iconSclass;
	}

	public void setIconSclass(String iconSclass) {
		this.iconSclass = iconSclass;
	}

	public boolean getClosable() {
		return closable;
	}

	public void setClosable(boolean closable) {
		this.closable = closable;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}