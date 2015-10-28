package cn.rkang.wxgate.ctrl.wxacc;

import java.util.ArrayList;
import java.util.List;

import cn.rkang.wxgate.model.WxAccMenu;

public class MenuTreeNodeVo {
	String					id;
	String					label;
	String					code;
	List<MenuTreeNodeVo>	childList	= new ArrayList<>();
	WxAccMenu				value;

	/**
	 * 以树型结构显示时，节点复选框是否选中
	 */
	private boolean			selected	= false;

	private boolean			open		= false;

	/**
	 * 用于构造树tree过程中判断node是否展开
	 * @return open为true或孩子被选中，则 展开
	 */
	public boolean getOpen() {
		return open || this.getChildSelected();
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Boolean getChildSelected() {
		if (this.childList != null) {
			for (MenuTreeNodeVo childOrgan : this.childList) {
				if (childOrgan.getSelected()) {
					return true;
				} else if (childOrgan.getChildSelected()) {
					return true;
				}
			}
		}
		return false;
	}

	public MenuTreeNodeVo(String id, String label, String code, boolean selected, boolean open) {
		super();
		this.id = id;
		this.label = label;
		this.code = code;
		this.selected = selected;
		this.open = open;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<MenuTreeNodeVo> getChildList() {
		return childList;
	}

	public void setChildList(List<MenuTreeNodeVo> childList) {
		this.childList = childList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public WxAccMenu getValue() {
		return value;
	}

	public void setValue(WxAccMenu value) {
		this.value = value;
	}
}