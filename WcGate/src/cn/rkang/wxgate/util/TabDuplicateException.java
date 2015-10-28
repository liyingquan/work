package cn.rkang.wxgate.util;

import org.zkoss.zul.Tab;

/**
 * 出现此异常，意味着用户试图打开一个id已经存在的tab页签
 * 无需处理此异常，直接返回即可，不要继续打开tabpanel
 */
@SuppressWarnings("serial")
public class TabDuplicateException extends Exception {
	private Tab	oldTab;

	public TabDuplicateException(String errorMsg, Tab tab) {
		super(errorMsg);
		this.oldTab = tab;
	}

	public Tab getOldTab() {
		return oldTab;
	}
}