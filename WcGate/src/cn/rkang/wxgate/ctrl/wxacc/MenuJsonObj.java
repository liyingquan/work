package cn.rkang.wxgate.ctrl.wxacc;

import java.util.ArrayList;
import java.util.List;

import cn.rkang.wxgate.model.WxAccMenu;

public class MenuJsonObj {

	List<WxAccMenu>	button	= new ArrayList<WxAccMenu>();

	public List<WxAccMenu> getButton() {
		return button;
	}

	public void setButton(List<WxAccMenu> button) {
		this.button = button;
	}

}
