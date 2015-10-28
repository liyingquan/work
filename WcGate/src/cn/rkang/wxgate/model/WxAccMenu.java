package cn.rkang.wxgate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信服务号定制菜单model（最多支持两层菜单）
 */
public class WxAccMenu {

	public enum MENU_TYPE {
		folder, click, view, scancode_waitmsg;
	}

	String			id;

	MENU_TYPE		type;

	String			name;

	String			key;

	String			url;

	String			parentId;

	List<WxAccMenu>	sub_button	= new ArrayList<WxAccMenu>();

	RespMessage		respMessage;

	public MENU_TYPE getType() {
		return type = type == MENU_TYPE.folder ? null : type;
	}

	public void setType(MENU_TYPE type) {
		this.type = type;
	}

	public String getTypeStr() {
		return type == null ? null : type.toString();
	}

	public void setTypeStr(String str) {
		type = MENU_TYPE.valueOf(str);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<WxAccMenu> getSub_button() {
		return sub_button;
	}

	public void setSub_button(List<WxAccMenu> sub_button) {
		this.sub_button = sub_button;
	}

	public RespMessage getRespMessage() {
		return respMessage;
	}

	public void setRespMessage(RespMessage respMessage) {
		this.respMessage = respMessage;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}