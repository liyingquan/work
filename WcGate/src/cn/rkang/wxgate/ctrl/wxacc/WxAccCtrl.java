package cn.rkang.wxgate.ctrl.wxacc;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Window;

import cn.rkang.wxgate.model.WxAcc;
import cn.rkang.wxgate.service.WxAccService;
import cn.rkang.wxgate.util.TabDuplicateException;
import cn.rkang.wxgate.util.TabVo;
import cn.rkang.wxgate.util.ZkUtils;

public class WxAccCtrl {
	static Logger	logger	= Logger.getLogger(WxAccCtrl.class);

	@Wire(value = "#mainTabs")
	Tabs			mainTabs;

	@Wire
	Tabpanels		mainTabpanels;

	@WireVariable
	WxAccService	wxAccService;

	List<WxAcc>		wxAccList;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		Selectors.wireEventListeners(view, this);

	}

	@Command
	public void onClickWxAccNavItem(@BindingParam("e") Event e, @BindingParam("appId") String appId) {
		Navitem navitem = (Navitem) e.getTarget();
		String navitemName = navitem.getLabel();
		logger.debug("onClick navitem【" + navitemName + "】" + "id[" + appId + "]");

		openTab4Acc(navitem, appId);
	}

	private void openTab4Acc(Navitem navitem, String appId) {
		String url = "/zuls/wxAcc/accMain.zul";
		//在center区域打开新页签
		TabVo tabNew = new TabVo();
		tabNew.setId("tab_" + url + appId);
		tabNew.setClosable(true);
		tabNew.setLabel(navitem.getLabel());
		tabNew.setIconSclass("z-icon-wechat");
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		try {
			Tabpanel tabpanel = ZkUtils.newTab(tabNew, url, mainTabs, mainTabpanels, ZkUtils.OverFlowType.AUTO,
					paramMap);
			logger.debug("open tabpanel:" + tabpanel.getId());
		} catch (TabDuplicateException e) {
			logger.info(e.getMessage());
		}
	}

	@Command
	public void onNewWxAccDialog() {
		Window win = (Window) Executions.createComponents("/zuls/wxAcc/addAccDialog.zul", null, null);
		win.doModal();
	}

	@GlobalCommand
	public void wxAccCtrlRefreshAccList() {
		BindUtils.postNotifyChange(null, null, this, "wxAccList");
	}

	public List<WxAcc> getWxAccList() {
		return wxAccService.getAllWxAcc();
	}

}